# Infraestrutura AWS

Este diretório contém a infraestrutura AWS definida em Terraform para o ambiente `dev`. A composição do ambiente fica em `environments/dev`; os componentes reutilizáveis ficam em `modules`. Os manifests Kubernetes da aplicação e o workflow de CI/CD estão fora deste diretório, em `k8s/` e `.github/workflows/ci-cd.yaml`.

> **Atenção:** esta configuração é voltada ao ambiente de desenvolvimento/demonstração. Ela usa subnets públicas, expõe a aplicação somente por HTTP, mantém o endpoint público do EKS habilitado e não configura um backend remoto para o state. Revise as recomendações de segurança e operação antes de utilizá-la em produção.

## Recursos provisionados

| Componente | Recursos e configuração atual |
| --- | --- |
| Rede | VPC `10.0.0.0/16`, duas subnets públicas (`10.0.1.0/24` em `us-east-1a` e `10.0.2.0/24` em `us-east-1b`), Internet Gateway e rota para `0.0.0.0/0`. Não há subnets privadas nem NAT Gateway. |
| EKS | Cluster `garageflow-dev`, versão configurada como `1.36`, endpoint da API público e privado, e um Managed Node Group com instância `t3.medium` do tipo Spot. O grupo mantém `desired/min/max = 1`. |
| Application Load Balancer | ALB público nas duas subnets, listener HTTP na porta 80, target group `instance` para a NodePort `30080` e health check em `/api/actuator/health`. |
| MongoDB | Instância EC2 Ubuntu 24.04, padrão `t3.small`, disco gp3 de 20 GB e MongoDB 7 em Docker. A senha aleatória de 24 caracteres é armazenada como `SecureString` no Systems Manager Parameter Store. A porta 27017 aceita conexões somente do Security Group dos nós do EKS. |
| Kubernetes | Namespace `garageflow`, Deployment, ConfigMap, Service NodePort, HPA e Secret criado durante o deploy. O Deployment solicita `200m` CPU e `384Mi`, com limites de `500m` e `768Mi`; o HPA mira 50% da CPU solicitada, de 1 a 3 pods. |

O HPA altera o número de **pods**, não o número de instâncias do node group. Como o node group está fixado em uma instância, o cluster pode não ter capacidade para agendar todos os pods em momentos de escala.

## Pré-requisitos

- Conta AWS e credenciais válidas por meio da cadeia padrão da AWS CLI (por exemplo, AWS IAM Identity Center/SSO ou variáveis de ambiente).
- Permissões para criar e consultar os recursos AWS utilizados pelo Terraform, incluindo VPC, EC2, EKS, IAM, ELB e SSM Parameter Store. A identidade usada no `kubectl` também precisa ter acesso administrativo ao cluster, conforme o `admin_role_arn`.
- Terraform, AWS CLI e `kubectl` instalados.
- Para o deploy automatizado: acesso ao GitHub Actions, GHCR e os secrets `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `JWT_SECRET` e `ADMIN_SEED_PASSWORD` configurados no repositório. O `JWT_SECRET` precisa atender ao requisito de tamanho da aplicação.

O padrão de região é `us-east-1`. O ARN da role que administrará o EKS é obrigatório e não tem valor padrão. Configure também sua autenticação AWS local antes de executar Terraform, por exemplo com `aws configure` ou `aws sso login --profile <perfil>`.

## Provisionar com Terraform

Execute os comandos a partir da raiz do repositório. O `fmt` abaixo formata os arquivos Terraform sob `infra/`; `init`, `validate`, `plan` e `apply` devem ser executados no diretório do ambiente:

```powershell
terraform -chdir=infra fmt -recursive
terraform -chdir=infra/environments/dev init
terraform -chdir=infra/environments/dev validate
terraform -chdir=infra/environments/dev plan -var="admin_role_arn=arn:aws:iam::<ACCOUNT_ID>:role/<ADMIN_ROLE>" -out=tfplan
terraform -chdir=infra/environments/dev apply tfplan
```

Substitua o ARN de exemplo por uma role existente na conta AWS e que será cadastrada como administradora do cluster. Se necessário, informe outras variáveis com `-var` ou por um arquivo local `*.tfvars`; não versione arquivos contendo credenciais ou valores sensíveis. Revise o plano antes de aplicar. O `tfplan` gerado também pode conter dados sensíveis: mantenha-o local e não o publique.

Os valores padrão estão em `environments/dev/variables.tf`: `aws_region = us-east-1`, `project_name = garageflow`, `environment = dev` e `kubernetes_version = 1.36`. O nome final do cluster é composto como `garageflow-dev`.

### State e segredos do Terraform

O ambiente não declara backend remoto, então o Terraform usa state local no diretório `infra/environments/dev`. A senha do Mongo é gerada pelo Terraform e seu valor pode estar presente no state e em arquivos de plano, mesmo sendo enviada ao SSM como `SecureString`. Trate esses arquivos como segredos: restrinja o acesso, não os envie ao Git ou a logs e faça backup seguro.

Para uso compartilhado ou produção, configure antes um backend remoto com criptografia, versionamento, controle de acesso e mecanismo de locking apropriado. Não mova nem apague um state existente sem planejar a migração e validar o estado dos recursos.

## Conectar ao EKS e verificar os outputs

Após o `apply`, ainda na raiz do repositório, atualize o kubeconfig e verifique o acesso:

```powershell
$clusterName = terraform -chdir=infra/environments/dev output -raw cluster_name
aws eks update-kubeconfig --region us-east-1 --name $clusterName
kubectl cluster-info
kubectl get nodes
```

Outputs úteis:

```powershell
terraform -chdir=infra/environments/dev output alb_dns_name
terraform -chdir=infra/environments/dev output cluster_name
terraform -chdir=infra/environments/dev output mongo_private_ip
terraform -chdir=infra/environments/dev output mongo_ssm_parameter
```

O endereço público da API é `http://<alb_dns_name>/api`. O output `mongo_private_ip` é um endereço interno da VPC; o Mongo não deve ser acessado diretamente pela Internet.

## Deploy da aplicação

O caminho recomendado é o workflow **CI/CD** em `.github/workflows/ci-cd.yaml`. Em pushes para `master` ou execução manual, ele:

1. Executa `mvn verify` com Java 25 e MongoDB de serviço.
2. Cria e publica a imagem no GHCR.
3. Autentica na AWS, conecta ao EKS e instala o `metrics-server`.
4. Lê a senha do Mongo no SSM, encontra o IP privado da instância e cria/atualiza o Secret Kubernetes a partir do SSM e dos GitHub Actions secrets.
5. Aplica os manifests, atualiza a imagem do Deployment, define `APP_PUBLIC_BASE_URL` com o DNS do ALB e aguarda o rollout.
6. Faz smoke tests do health check e do login administrativo pelo ALB.

Para o workflow funcionar, os nomes dos recursos e a região configurados nele devem corresponder ao ambiente Terraform. A identidade AWS das credenciais do workflow deve ter as permissões necessárias e estar autorizada no EKS; alinhe essa identidade com o `admin_role_arn` configurado no Terraform ou ajuste o fluxo de autenticação para assumir a role apropriada.

O arquivo `k8s/app/secret.yaml` contém valores de exemplo/placeholder. **Não aplique esse Secret em um ambiente real**: o workflow cria o Secret com valores reais. Os demais manifests podem ser aplicados manualmente, mas somente depois de criar um Secret real com as chaves exigidas pelo Deployment:

```powershell
kubectl apply -f k8s/namespace.yaml
# Crie garageflow-app-secret com valores reais por um mecanismo seguro antes do Deployment.
kubectl apply -f k8s/app/configmap.yaml
kubectl apply -f k8s/app/service.yaml
kubectl apply -f k8s/app/hpa.yaml
kubectl apply -f k8s/app/deployment.yaml
kubectl get pods,svc,hpa -n garageflow
```

O HPA requer métricas do cluster; o workflow instala o `metrics-server`. Em um deploy manual, confirme que há um provedor de métricas operacional antes de esperar que o HPA escale.

## Segurança, disponibilidade e custos

- **Sem TLS:** o ALB aceita HTTP/80 e não há listener HTTPS nem certificado configurado. Credenciais, tokens e dados da aplicação não devem trafegar em produção sem TLS.
- **Subnets públicas:** EKS e EC2 do Mongo são associados às subnets públicas. A instância Mongo recebe IP público para saída, mas seu Security Group não permite entrada da Internet na porta 27017. Considere subnets privadas e saída controlada para uma arquitetura de produção.
- **Regra dos nós ampla:** o Security Group dos nós aceita tráfego TCP nas portas `0–65535` originado do Security Group do ALB. Restrinja portas e origens ao mínimo necessário.
- **EKS público:** o endpoint público do control plane está habilitado. Restrinja os endereços de origem ou desabilite o acesso público conforme os requisitos do ambiente.
- **Capacidade Spot fixa:** o node group usa uma instância Spot e não escala além de um nó. Interrupções Spot afetam a disponibilidade; a configuração atual não fornece alta disponibilidade dos nós.
- **Persistência do Mongo:** o banco usa uma única instância e um volume de 20 GB que é removido com a instância. Não há backup/replicação configurados. Planeje backups e recuperação antes de armazenar dados importantes.
- **Exposição de segredos:** proteja state, planos e credenciais AWS. Use permissões mínimas no SSM/IAM e secrets do GitHub; não versione valores reais em manifests, `.tfvars` ou logs.
- **Custos:** EKS control plane, EC2, volumes, ALB, tráfego e demais recursos podem gerar cobrança enquanto existirem. A instância Spot pode variar em disponibilidade; confirme preços e cotas na região antes de provisionar.

## Destruir o ambiente

`terraform destroy` remove os recursos gerenciados por este state e pode causar perda permanente dos dados do MongoDB e do parâmetro de senha. Faça backup dos dados e confirme o workspace/diretório e o plano de destruição antes de executar:

```powershell
terraform -chdir=infra/environments/dev plan -destroy -var="admin_role_arn=arn:aws:iam::<ACCOUNT_ID>:role/<ADMIN_ROLE>"
terraform -chdir=infra/environments/dev destroy -var="admin_role_arn=arn:aws:iam::<ACCOUNT_ID>:role/<ADMIN_ROLE>"
```
