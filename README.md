#  Online Grocery Store Management System

A **Spring Boot + MySQL backend project** for managing a real-world Kirana (grocery) store in Daund.  
The system enables product browsing, cart management, secure payments via **Razorpay**, and store location integration using **Google Maps API**.  

Built as a **final-year project** and inspired by real-world business needs.  

---

## Features

- **User Authentication & Authorization**
  - JWT-based login & token validation.
  - Role-Based Access Control (RBAC): 
    - `ADMIN` → Manage stock, prices, products.  
    - `CUSTOMER` → Browse, add to cart, place orders.

- **Product & Cart Management**
  - CRUD operations for products.
  - Add/remove items from cart.
  - Checkout & place orders.

- **Payments (Razorpay Integration)**
  - Secure UPI/Card payments.
  - Backend verification of signatures.
  - Webhook integration for order confirmation.

- **Google Maps Integration**
  - Store location stored as latitude/longitude.
  - Customers can locate shop via Google Maps.

- **System Design**
  - RESTful APIs with clean architecture.
  - Exception handling & validation.
  - Database schema optimized for scalability.

---

## Tech Stack

- **Backend:** Java, Spring Boot, Spring Security (JWT)
- **Database:** MySQL  
- **Payment Gateway:** Razorpay  
- **Maps API:** Google Maps Geocoding  
- **Build Tool:** Maven  
- **Tools:** Git, Postman, Docker (optional)  

---

## 🚀 Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/icas00/Grocery-Store-Management-.git
cd Grocery-Store-Management-
