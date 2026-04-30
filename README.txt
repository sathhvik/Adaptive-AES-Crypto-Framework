AES Spring Boot Project (ready-to-run)
-------------------------------------

How to build:
1. Ensure Java 17 and Maven are installed.
2. Unzip this archive and open a terminal in the project root.
3. Run: mvn clean package
4. Run: java -jar target/aes-app-1.0.0.jar
5. Open: http://localhost:8080

Notes:
- Uploaded/encrypted/decrypted files are stored in a folder named 'uploads' next to where you run the app.
- Keep the Base64 secret key shown after encryption; you'll need it to decrypt.
- This project is a student project example and not production hardened. Do not use as-is for sensitive data.
