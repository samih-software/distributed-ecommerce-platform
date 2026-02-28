# distributed-ecommerce-platform
Personal distributed e-commerce platform with a Spring Boot REST API, separate delivery service, and React front-end. Includes authentication (JWT), payments (Stripe), and MySQL database.

## System Design Architecture
Overview of the system architecture showing the components, data flow, and interactions within the application.

![System Design Architecture](https://github.com/user-attachments/assets/a130f794-fe47-4521-bdd5-103f35435f34)

## Components

### 1. Front-End: Client Website
- **Description:** Web interface where customers browse products, place orders, and initiate payments. (React.js)
- **Interactions:** Sends REST + JWT requests to the E-Commerce API.

### 2. E-Commerce API
- **Description:** Backend API managing products, orders, and payments.  
- **Responsibilities:**  
  - Handles requests from the front-end.  
  - Initiates payments via Stripe and processes webhooks.  
  - Interacts with MySQL database for product and order data.

### 3. Stripe Payment
- **Description:** Third-party payment processor.  
- **Interactions:**  
  - Processes payments initiated from the front-end.  
  - Sends webhook notifications to the E-Commerce API upon success/failure.  
- [Stripe Documentation](https://docs.stripe.com/connect)

### 4. Delivery API
- **Description:** Manages delivery requests, routes, and status updates.  
- **Responsibilities:**  
  - Receives coordinates from Nominatim service.  
  - Calculates delivery routes using OSRM.  
  - Stores delivery data in Apache Cassandra.

### 5. Nominatim Service
- **Description:** Geocoding service converting addresses to latitude and longitude.  
- **Deployment:** Docker container.  
- **Interactions:** Provides coordinates to the Delivery API.  
- [Nominatim Docs](https://nominatim.org/release-docs/latest/admin/Installation/)

### 6. OSRM Service
- **Description:** Routing engine calculating delivery routes.  
- **Deployment:** Docker container.  
- **Interactions:** Provides route distance and estimated delivery time to the Delivery API.  
- [OSRM API Docs](https://project-osrm.org/docs/v5.24.0/api/#)

### 7. Databases
- **MySQL Database:** Stores product info, customer info, and orders. Accessed by E-Commerce API via Hibernate. Migration files managed with Flyway.  
  - [MySQL Documentation](https://dev.mysql.com/doc/)
- **Apache Cassandra Database:** Stores delivery-related data. Accessed by Delivery API via Hibernate. CQL script run on Docker to generate databases.  
  - [Cassandra Documentation](https://cassandra.apache.org/doc/latest/cassandra/installing/installing.html)

## Website Flow

A quick overview of the user journey through the website: from landing to order completion.

---

### 0. Start Page
Landing page where users can explore the platform or navigate to login.

![Start Page](https://github.com/user-attachments/assets/9639ce0b-00b7-451d-ae35-88de92c70cd6)

---

### 1. Login
Secure login page to access the user account.

![Login Page](https://github.com/user-attachments/assets/ef057e99-43bc-49ca-ba45-698b6058a476)

---

### 2. Order Page
Browse products, select items, and add to cart.

![Order Page](https://github.com/user-attachments/assets/b821f85f-44ce-4234-bc5b-9e5539dc17b6)

---

### 3. Checkout Page
Review order summary and confirm items.

![Checkout Page](https://github.com/user-attachments/assets/74c360a7-25a4-4a09-a7e5-3b1d0776edc4)

---

### 4. Stripe Payment Page
Secure payment processing via Stripe.

![Stripe Payment Page](https://github.com/user-attachments/assets/62979885-0e50-4230-ad73-c0c473d5ae2d)

---

### 5. Order Verification Page
Confirmation page showing successful order and order details.

![Order Verification Page](https://github.com/user-attachments/assets/865091bc-bcda-44e2-945e-12fa9010470b)

---

### 6. Order History & Delivery Info
View past orders, track deliveries, and see detailed order information.

![Order History Page](https://github.com/user-attachments/assets/6eeafeb1-3d50-4959-8294-caa8affb0a0f)


## Tests
Code includes tests for: authentication and order flow tests

![Test Code / Test Results](https://github.com/user-attachments/assets/87113b9a-15a9-4de4-aae9-c6ffe8e377df)

