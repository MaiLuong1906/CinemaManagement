# Design Guidelines

## User Interface Philosophy
The Cinema Management System aims for a clean, professional, and accessible interface. The design focuses on high-impact visual elements (movie posters) and a streamlined booking flow to minimize friction for the customer.

## UI/UX Patterns

### 1. The Booking Flow
- **Progressive Disclosure**: Information is presented in steps (Movie -> Showtime -> Seats -> Products -> Payment) to avoid overwhelming the user.
- **Visual Feedback**: Real-time updates during seat selection (color changes for selected/booked/available seats).
- **Responsive Layouts**: Using Bootstrap's grid system to ensure usability on mobile, tablet, and desktop devices.

### 2. Component Structure
- **Global Header/Footer**: Consistent navigation across all pages using `<jsp:include>`.
- **Modals**: Used for quick actions (e.g., login, movie trailers) to keep the user in context.
- **Admin Sidebars**: A dedicated navigation rail for administrative functions to maximize screen real estate for data tables and charts.

## Frontend Technologies

### Bootstrap Integration
- **Grid System**: Use `.container`, `.row`, and `.col-*` classes for all structural layouts.
- **Components**: Leverage Bootstrap components like cards for movie listings, buttons for actions, and forms for input.
- **Migration Note**: The project is transitioning from Bootstrap 3 to Bootstrap 5. New components should use Bootstrap 5 syntax exclusively.

### Styling
- **Custom CSS**: Specific styling for the seat map and specialized UI elements is located in `src/main/webapp/css/`.
- **Icons**: Use Font Awesome (v5/v6) for intuitive visual cues (e.g., trash icon for delete, pencil for edit).

## JavaScript Guidelines
- **jQuery**: Used for DOM manipulation, event handling, and AJAX calls (e.g., seat holding logic).
- **Validation**: Client-side validation for all forms to provide immediate feedback.
- **Asynchronicity**: Use AJAX for actions that shouldn't require a full page reload, such as adding products to the cart or checking seat status.

## Visual Vocabulary
- **Primary Color**: Dark themes are preferred for cinema applications to mimic the theater environment.
- **Action Colors**:
  - `btn-primary`: Primary actions (Booking, Submit).
  - `btn-danger`: Destructive actions (Delete, Cancel).
  - `btn-success`: Positive outcomes (Payment success, Save).
- **Typography**: Clean, sans-serif fonts for readability.
