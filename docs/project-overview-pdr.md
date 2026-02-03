# Project Overview & Product Development Requirements (PDR)

## Project Vision
The Cinema Management System is a comprehensive digital platform designed to streamline movie theater operations and enhance the customer booking experience. It provides a seamless transition from movie discovery to seat selection and secure payment, while offering robust administrative tools for theater management.

## Objectives
- Provide an intuitive, responsive interface for customers to book movie tickets and concessions.
- Enable theater administrators to manage movies, showtimes, halls, and pricing efficiently.
- Integrate secure, real-time payment processing (VNPAY).
- Generate actionable business insights through revenue and occupancy analytics.

## Target Users
| User Group | Goals |
|------------|-------|
| **Customers** | Browse movies, select seats, purchase tickets/snacks, and view booking history. |
| **Administrators** | Manage catalog, schedule showtimes, monitor revenue, and manage user accounts. |

## Key Features

### Customer Features
- **Movie Discovery**: Browse current and upcoming movies with detailed descriptions and trailers.
- **Seat Selection**: Interactive seat map with real-time availability and different seat tiers (Normal, VIP, Coupe).
- **Concession Booking**: Add snacks and drinks to the ticket booking.
- **Payment Integration**: Secure payment via VNPAY with instant booking confirmation.
- **User Profile**: Manage personal information and view past/upcoming bookings.

### Administrative Features
- **Movie Management**: Full CRUD operations for the movie catalog, including poster uploads.
- **Showtime Scheduling**: Conflict-aware scheduling of movies in specific halls and time slots.
- **Hall & Seat Management**: Configure hall layouts and seat types.
- **Analytics Dashboard**: Real-time tracking of revenue, ticket sales, and seat occupancy rates.
- **User Management**: Monitor and manage user accounts and permissions.

## Business Requirements
1. **Transaction Integrity**: Ensure seats are held during the booking process and released if payment fails.
2. **Scheduling Constraints**: Prevent overlapping showtimes in the same hall.
3. **Financial Reporting**: Accurate tracking of revenue by movie, time slot, and date.
4. **Security**: Protected administrative routes and secure storage of user credentials.

## Success Metrics
- **Booking Efficiency**: Average time from movie selection to payment completion.
- **System Reliability**: Uptime during peak booking hours.
- **Financial Accuracy**: 100% reconciliation between system bookings and VNPAY transactions.
- **User Engagement**: Conversion rate from movie detail views to completed bookings.
