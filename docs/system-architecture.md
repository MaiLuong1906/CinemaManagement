# System Architecture

## 3-Tier Architecture Overview

The system is structured into three distinct layers to ensure separation of concerns and maintainability.

| Layer | Responsibility | Components |
|-------|----------------|------------|
| **Presentation** | User Interface & Request Handling | JSP, JSTL, Servlets, CSS, Bootstrap, jQuery |
| **Business Logic** | Application Rules & Orchestration | Service Classes, Utility Classes, Filters |
| **Data Access** | Persistence & Data Retrieval | DAOs, DTOs, SQL Server Views, JDBC |

## Component Interaction Diagram

```text
[ Browser ]
     │
     ▼
[ Servlets (Controller) ] <───> [ Filters (Security) ]
     │
     ▼
[ Services (Logic) ]
     │
     ▼
[ DAOs (Data Access) ] <───> [ SQL Server ]
     │
     ▼
[ Models / DTOs ] (Data Containers)
```

## Data Flow: Booking Operation

1. **Selection**: User selects seats in `seat-selection.jsp`.
2. **Hold**: `HoldSeatServlet` updates the user session with selected seat IDs.
3. **Creation**: `ConfirmBookingServlet` creates an `Invoice` record and associated `TicketDetail` records within a database transaction.
4. **Payment**: `ajaxServlet` constructs a VNPAY payment URL using `VNPAYConfig`.
5. **Callback**: VNPAY redirects to `PaymentReturnServlet`.
6. **Finalization**: `PaymentReturnServlet` verifies the checksum and updates the `Invoice` status to 'Paid'.

## Database Schema Overview

The system uses a highly normalized relational schema (3NF) with optimized views for reporting.

### Core Entities
- **Accounts & Profiles**: 1:1 relationship for authentication and user details.
- **Movies & Genres**: M:N relationship via `movie_genre_rel`.
- **Halls & Seats**: 1:M relationship defining physical theater layouts.
- **Showtimes**: Central entity linking Movies, Halls, and Time Slots.
- **Invoices**: Header table for transactions, linked to `TicketDetail` and `ProductDetail`.

### Analytics Layer (SQL Views)
The system leverages database-side processing for performance:
- `vw_movie_showtime_detail`: Flattened view of scheduling data.
- `vw_seat_coverage_detail`: Calculates occupancy percentage per showtime.
- `vw_ticket_sold_by_slot_current_month`: Monthly performance metrics.

## Integration Architecture

### VNPAY Integration
- **Protocol**: Secure redirect with HMAC-SHA512 hashing.
- **Configuration**: Centralized in `config/VNPAYConfig.java`.
- **Flow**: Asynchronous notification (IPN) support through callback handling.

### File System Integration
- **Movie Posters**: Served via `ImageServlet` from a configurable disk path.
- **Constraint**: Currently hardcoded to `C:/imgForCinema`, representing a technical debt item for portability.
