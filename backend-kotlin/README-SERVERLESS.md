# Serverless Deployment for Kotlin Backend

This document describes how to deploy the Micronaut Kotlin backend using the Serverless Framework.

## Prerequisites

- Java 21 or higher
- AWS CLI configured with appropriate credentials
- Serverless Framework CLI installed globally: `npm install -g serverless` or use `npx serverless`

## Setup

1. Build the Kotlin application:
   ```bash
   ./gradlew build
   ```

## Deployment

### Deploy to AWS
```bash
serverless deploy
```

### Remove from AWS
```bash
serverless remove
```
