# 🎬 Cinema Management System

<div align="center">
  <img src="https://img.shields.io/badge/Java-17-orange.svg" alt="Java"/>
  <img src="https://img.shields.io/badge/Jakarta_EE-10.0-blue.svg" alt="Jakarta"/>
  <img src="https://img.shields.io/badge/Database-SQL_Server-red.svg" alt="SQL Server"/>
  <img src="https://img.shields.io/badge/AI-LangChain4j-yellow.svg" alt="LangChain4J"/>
  <img src="https://img.shields.io/badge/Build-Maven-C71A36.svg" alt="Maven"/>
</div>

<p align="center">
  <strong>A comprehensive and intelligent Java-based Web Application for managing cinema operations, ticketing, user profiles, and revenue forecasting.</strong>
</p>

---

## 📖 Table of Contents

- [Introduction](#-introduction)
- [Key Features](#-key-features)
- [Technology Stack](#-technology-stack)
- [Architecture](#-architecture)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Configuration](#configuration)
- [AI Assistant Configuration](#-ai-assistant-configuration)
- [Screenshots](#-screenshots)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🌟 Introduction

The **Cinema Management System** is a robust MVC web application built with **Java Servlet (Jakarta EE)**. It covers everything from movie schedules and seat booking to concessions, invoicing, and advanced analytics. Additionally, it integrates an **AI Chatbot API** using Groq and Langchain4J to provide a modern interactive experience.

---

## 🎯 Key Features

### For Customers (Users)
- **Account Management**: Registration, login, profile adjustments.
- **Browse Movies & Showtimes**: Real-time listings of playing and upcoming movies.
- **Seat Booking**: Interactive layout for selecting seats (Standard, VIP, etc.).
- **Concessions & Products**: Add food and beverages to movie tickets.
- **Booking History & Invoices**: View past bookings and details.

### For Administrators (Managers)
- **Movie & Genre Management**: Add, update, or remove movies.
- **Showtime & TimeSlot Scheduling**: Dynamically assign movies to cinema halls.
- **Cinema Hall & Seat Configuration**: Scale and edit seating templates.
- **Reporting & Analytics**:
  - Live dashboards for **Seat Fill Rate**, **Tickets Sold**, and **Revenue**.
  - Advanced **Forecasting** using historical data.
  - **Exporting Reports** to Microsoft Excel (.xlsx).

### Intelligent Features
- **AI Integration**: Powered by Groq LLM to handle user inquiries dynamically.

---

## 💻 Technology Stack

### Backend
- **Core language**: Java 17
- **Web Framework**: Jakarta EE (Servlet 6.0, JSP)
- **Database**: Microsoft SQL Server
- **Connection Pool**: HikariCP
- **AI Integration**: LangChain4j & Groq
- **JSON Processing**: Jackson & Gson
- **Excel Export**: Apache POI

### Frontend
- **Languages**: HTML5, CSS3, JavaScript
- **Templating**: JSP with JSTL
- **Styling**: Custom CSS / Frameworks

### Testing & Tools
- **Unit Testing**: JUnit 5, Mockito
- **In-memory DB**: H2 Database (for testing)
- **Build Tool**: Maven

---

## ⚙️ Architecture

The project follows the standard **MVC (Model-View-Controller)** pattern:
- **Model**: Data Access Entities, DTOs, and DAOs.
- **View**: JSP pages located under `src/main/webapp/views`.
- **Controller**: Java Servlets intercepting user requests.
- **Service Layer**: Contains core business logic and computational operations.

---

## 🚀 Getting Started

### Prerequisites
- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or higher
- [Maven](https://maven.apache.org/download.cgi) 3.8+
- [Microsoft SQL Server](https://www.microsoft.com/en-us/sql-server/sql-server-downloads) (or equivalent local instance)
- Support IDE (IntelliJ IDEA, Eclipse, NetBeans) or Tomcat 10+ server setup.

### Installation
1. **Clone the repository:**
   ```bash
   git clone https://github.com/MaiLuong1906/CinemaManagement.git
   cd CinemaManagement
   ```
2. **Setup Database:**
   - Navigate to the `SQL_version1_english` folder (or relevant SQL directory).
   - Execute the SQL script in SQL Server to create the `CinemaManagement` schema and insert seed data.
3. **Build the project using Maven:**
   ```bash
   mvn clean install
   ```

### Configuration
Update the database connection properties. 
Navigate to `src/main/resources/db.properties` (or your config file mapped to it) and adjust:
```properties
db.url=jdbc:sqlserver://localhost:1433;databaseName=CinemaManagement;encrypt=true;trustServerCertificate=true
db.username=sa
db.password=your_sqlserver_password
db.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
```

---

## 🤖 AI Assistant Configuration

To enable the AI features for recommendations and chatbot:
1. Register for a free API key at [console.groq.com](https://console.groq.com/).
2. Add your key to `db.properties` (or your application configuration):
   ```properties
   ai.api.key=gsk_your_api_key_here
   ```

---

## 📸 Screenshots

| Home Page | Seat Booking |
|:---:|:---:|
| <img src="https://via.placeholder.com/600x350?text=Home+Page" alt="Home page" width="100%"> | <img src="https://via.placeholder.com/600x350?text=Seat+Booking" alt="Booking interface" width="100%"> |

| Admin Dashboard | Analytics & Forecast |
|:---:|:---:|
| <img src="https://via.placeholder.com/600x350?text=Dashboard" alt="Dashboard" width="100%"> | <img src="https://via.placeholder.com/600x350?text=Excel+Export+&+Stats" alt="Stats" width="100%"> |

*(Note: Replace the placeholder images above with actual screen captures of the project running by uploading your images to the repo and linking them here).*

---

## 🤝 Contributing
Contributions, issues, and feature requests are welcome!
Feel free to check out the [issues page](https://github.com/MaiLuong1906/CinemaManagement/issues).

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License
Distributed under the MIT License. See `LICENSE` for more information.
