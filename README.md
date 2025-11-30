<div align="center">

# Vanish Link ⚡

**Self-Destructing URL Shortener**  
One-time view • Auto-expiry • Password protection • Dockerized

[![Docker](https://img.shields.io/badge/Docker-Ready-blue?style=for-the-badge&logo=docker)](https://docker.com)
[![Redis](https://img.shields.io/badge/Redis-TTL-red?style=for-the-badge&logo=redis)](https://redis.io)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Permanent-316192?style=for-the-badge&logo=postgresql)](https://postgresql.org)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)

**Live Demo** → https://vanish-link.up.railway.app (coming in 5 mins da)

</div>

## Features

- Burn after reading (one-time view)  
- Auto-expiry via Redis TTL (5 mins, 1 hour, 24 hours, 7 days)  
- Optional password protection (BCrypt)  
- Click analytics  
- Fully Dockerized (PostgreSQL + Redis + Spring Boot)  
- No cron jobs — pure Redis magic  
- Collision-free 7-char Base62 codes

## Quick Start (10 seconds)

```bash
git clone https://github.com/kadhar3425/vanish-link.git
cd vanish-link
docker-compose up -d
./mvnw spring-boot:run