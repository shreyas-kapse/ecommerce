
# E-Commerce Backend

**E-Commerce Backend** is a full-featured backend solution for modern e-commerce websites, built with **Spring Boot** and leveraging **PostgreSQL**. It offers role-based **JWT authentication**, secure email verification, and seamless integration with APIs via **Swagger OpenAPI 3**. Whether you're a developer building a new platform or expanding an existing store, E-Commerce Backend is designed for reliability and scalability.

## 🌟 Key Features

1. **🔐 Secure JWT Authentication**:  
   Provides **role-based access** for both users and merchants. Upon successful login, a **JWT token** is issued for secure interactions with the platform.

2. **📧 Email Verification**:  
   Upon account creation, users are required to verify their email address via a link sent to their inbox. Merchant accounts are additionally verified by an admin.

3. **📬 Email Notifications**:  
   Real-time email notifications are triggered on key actions like **login** and **order placement**, keeping users informed.

4. **🛒 Order Management System**:  
   - **For Users**: Seamless order placement process.  
   - **For Merchants**: Merchants can view and manage all incoming orders.

5. **🔍 Advanced Product Browsing**:  
   Users can filter products based on:
   - Category
   - Brand Name
   - Merchant Name

6. **📜 API Documentation**:  
   Comprehensive **Swagger OpenAPI 3** integration for easy testing and exploration of API endpoints.

7. **💾 Robust Database**:  
   **PostgreSQL** is implemented as the database, ensuring data integrity, scalability, and high performance.

## ⚙️ Technologies Used

- **Java**: Version 17+  
- **Spring Boot**: Version 3.x  
- **PostgreSQL**: Version 15+  
- **JWT**: For secure authentication  
- **Swagger OpenAPI 3**: For API documentation  

## 🚀 Installation

Follow these steps to set up and run the E-Commerce Backend:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/shreyas-kapse/ecommerce.git
   cd ecommerce
   ```

2. **Configure your PostgreSQL database**:
   Update the `.env` file with your PostgreSQL credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/yourdbname
   spring.datasource.username=yourusername
   spring.datasource.password=yourpassword
   ```

3. **Build the project**:
   ```bash
   ./gradlew build
   ```

4. **Run the application**:
   ```bash
   ./gradlew bootRun
   ```

5. **Access the Swagger API Documentation**:  
   Once the server is running, navigate to `http://localhost:8080/swagger-ui.html` to explore the API.

## 📜 API Documentation

**Swagger OpenAPI 3** is fully integrated into E-Commerce Backend. You can interact with and test all available endpoints directly from the **Swagger UI**.

To access the documentation:
- Start the application and go to `http://localhost:8080/swagger-ui.html`.

### Example Endpoints:

- **Login**: `/login`
- **Create User**: `/register`
- **Place Order**: `/orders/place`
- **View Products by Category**: `/products/category/{category}`

## 🔮 Future Enhancements

1. **Merchant Analytics**:  
   We plan to introduce detailed analytics dashboards for merchants, enabling better insights into sales, product performance, and customer behaviors.

2. **Improved User Features**:  
   Users will soon enjoy features like:
   - Product reviews and ratings
   - Personalized recommendations
   - Enhanced search filters

3. **Multi-Currency Support**:  
   The platform will support multiple currencies, expanding its reach to international markets.

4. **Advanced Security Features**:  
   Enhancements in security, such as multi-factor authentication (MFA), will be implemented to further protect user accounts.

## 📜 License

This project is licensed under the MIT License.

## 🔗 Connect with Us

Stay updated and connect with our community:

- [GitHub Repository](https://github.com/shreyas-kapse/ecommerce)
- [LinkedIn Profile](https://www.linkedin.com/in/skapse/) 

