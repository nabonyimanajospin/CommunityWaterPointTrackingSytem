# Community Water Point Tracking System

A Java desktop system for tracking community water points, reporting faults and managing repairs.

## Structure

| Folder | Description |
|--------|-------------|
| `CommunityWaterPointTrackingSystemServer26511` | Server: Hibernate DAO, services (maintenance requests, teams, notifications), report generation |
| `CommunityWaterPointTrackingSystemClient26511` | Swing client: login, water point management, repair tracking, RMI client, message consumer |
| `apache-activemq-5.16.6`, `apache-activemq-6.2.0` | Apache ActiveMQ brokers used for messaging |

## Technologies

- Java (NetBeans / Ant projects)
- Hibernate 5.6 with c3p0 connection pooling
- Java RMI for client-server calls
- Apache ActiveMQ for notifications
- iText and Apache POI for PDF / Excel reports

## Running

1. Start an ActiveMQ broker (`apache-activemq-6.2.0/bin`).
2. Configure the database connection in the server's Hibernate settings.
3. Open both projects in NetBeans and run the Server, then the Client.
