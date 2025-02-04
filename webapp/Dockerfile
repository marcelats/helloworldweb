# Dockerfile for mqproducer
FROM node:20-slim

WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm ci
# Copy the rest of the application files
COPY . .

RUN npm run build


# Expose any necessary ports here
# Example port, adjust as needed
EXPOSE 3000  


# Run the application
CMD ["npm", "run", "start"]
