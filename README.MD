## CSV File Format

## <mark>The CSV file should contain historical values for trades in the following format<mark>

    
    Id,Value1,Value2,Value3,Value4,Value5,Value6,Value7,Value8,Value9,Value10
    1,100,101,102,103,104,105,106,107,108,109
An example CSV file (`trades.csv`) is included in the repository.
## CSV File Format

## <mark>The CSV file should contain historical values for trades in the following format<mark>

    
    Id,Value1,Value2,Value3,Value4,Value5,Value6,Value7,Value8,Value9,Value10
    1,100,101,102,103,104,105,106,107,108,109
An example CSV file (`trades.csv`) is included in the repository.
# VaR Calculator Backend

This is the backend service for calculating Value at Risk (VaR). It is implemented using Spring Boot.

## Installation Instructions

### Prerequisites

- Java 17
- Maven

### Setup

1. Clone the repository:
    ```sh
    git clone https://github.com/Maazouz-Zouhair/VaR.git
    cd var-gms-backend
    ```

2. Build the project:
    ```sh
    mvn clean install
    ```

3. Run the application:
    ```sh
    mvn spring-boot:run
    ```

The application will start at `http://localhost:8080`.

## API Endpoints

### Inference of Mode (SingleTrade/Portfolio):

- **URL:** `POST /api/var/calculate`
- **Body:**
    - `file` (multipart/form-data): The CSV file containing historical values for a single trade.
    - `confidenceLevel` (text): The confidence level for VaR calculation (e.g., 0.95).

## Dependencies

- Spring Boot
- Spring Web
- Spring Validation
- jakarta.validation
- org.hibernate.validator
- org.apache.commons / commons-csv / version : 1.8
- spring boot devtools

# VaR Calculator Frontend

It is implemented using Angular.

## Installation Instructions

### Prerequisites

- Node.js v20.15.0
- Angular CLI v18

### Setup

1. Clone the repository:
    ```sh
    git clone https://github.com/Maazouz-Zouhair/VaR.git
    cd var-gms-frontend
    ```

2. Install dependencies:
    ```sh
    npm install
    ```

3. Run the application:
    ```sh
    ng serve
    ```

The application will start at `http://localhost:4200`.

## Usage

1. Open the application in your browser at `http://localhost:4200`.
2. Upload the CSV file and specify the confidence level.
4. Click "Calculate VaR" to get the result.

## Dependencies

- Angular 18
- Bootstrap 
