package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.document;

public class AverageWorkshopServiceExecutionTimeDocument {

        private String workshopServiceId;

        private String description;

        private long completedServices;

        private double averageDurationInMinutes;

        private Integer minimumDurationInMinutes;

        private Integer maximumDurationInMinutes;

        public String getWorkshopServiceId() {
            return workshopServiceId;
        }

        public void setWorkshopServiceId(
                String workshopServiceId) {
            this.workshopServiceId = workshopServiceId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(
                String description) {
            this.description = description;
        }

        public long getCompletedServices() {
            return completedServices;
        }

        public void setCompletedServices(
                long completedServices) {
            this.completedServices = completedServices;
        }

        public double getAverageDurationInMinutes() {
            return averageDurationInMinutes;
        }

        public void setAverageDurationInMinutes(
                double averageDurationInMinutes) {
            this.averageDurationInMinutes =
                    averageDurationInMinutes;
        }

        public Integer getMinimumDurationInMinutes() {
            return minimumDurationInMinutes;
        }

        public void setMinimumDurationInMinutes(
                Integer minimumDurationInMinutes) {
            this.minimumDurationInMinutes =
                    minimumDurationInMinutes;
        }

        public Integer getMaximumDurationInMinutes() {
            return maximumDurationInMinutes;
        }

        public void setMaximumDurationInMinutes(
                Integer maximumDurationInMinutes) {
            this.maximumDurationInMinutes =
                    maximumDurationInMinutes;
        }
    }