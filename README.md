# 🔐 Adaptive Web-Based Cryptographic Framework using AES

A secure and scalable web-based cryptographic system that implements **AES encryption with dynamic key generation**, enhanced with **QR code-based key sharing** and **steganography for hidden key storage**.

---

## 📌 Overview

This project provides a complete encryption-decryption pipeline for files (text & images) using the **Advanced Encryption Standard (AES)**. Unlike traditional systems that rely on static keys, this framework generates **dynamic keys** and enforces **strong key validation**, significantly improving security.

The system is built using a **three-tier architecture**:

* Presentation Layer (Thymeleaf UI)
* Business Logic Layer (Spring Boot)
* Storage Layer (Temporary file system)

---

## 🚀 Key Features

* 🔐 **AES Encryption & Decryption**

  * AES/CBC/PKCS5Padding mode
  * Random IV for each encryption
  * Secure and reversible process

* 🔑 **Dynamic Key Generation**

  * Auto-generated AES keys (Base64 encoded)
  * Supports AES-128, AES-192, AES-256

* 🛡️ **Strong Key Validation**

  * Enforces uppercase, lowercase, numbers, symbols
  * Prevents weak key usage

* 📷 **QR Code Key Sharing**

  * Converts encryption keys into QR codes
  * Easy and secure key distribution

* 🖼️ **Steganography (Key Hiding)**

  * Hides encryption keys inside images using LSB technique
  * Invisible and secure key storage

* 🔍 **Key Extraction Module**

  * Retrieves hidden key from stego images
  * Uses delimiter-based extraction

* 🌐 **User-Friendly Web Interface**

  * Built with Thymeleaf
  * Simple upload/download workflow

---

## 🏗️ System Architecture

The system follows a **three-tier architecture**:

```
Browser (UI)
   ↓
Spring Boot Backend (Controllers + Services)
   ↓
Temporary File Storage (Encrypted / Output Files)
```

---

## 🔄 Workflow

### Encryption Flow

1. Upload file + enter key
2. Key validation
3. Random IV generation
4. AES encryption
5. Output: `.aes` file

### Decryption Flow

1. Upload `.aes` file
2. Extract IV
3. AES decryption
4. Output: original file

---

## 🧠 Technologies Used

* **Backend:** Java, Spring Boot
* **Frontend:** Thymeleaf
* **Encryption:** AES (javax.crypto)
* **QR Code:** ZXing Library
* **Steganography:** LSB Image Processing
* **Build Tool:** Maven
* **OS Support:** Windows / Linux / macOS

---

## 📂 Project Structure

```
src/
 ├── controller/
 ├── service/
 ├── templates/
 └── static/

pom.xml
README.md
```

---

## ▶️ How to Run

1. Clone the repository:

```bash
git clone https://github.com/your-username/adaptive-aes-crypto-framework.git
```

2. Navigate to project:

```bash
cd adaptive-aes-crypto-framework
```

3. Run the application:

```bash
mvn spring-boot:run
```

4. Open in browser:

```
http://localhost:8080
```

---

## 🔒 Security Highlights

* Uses **random IV per encryption** (prevents pattern attacks)
* Ensures **strong password-based keys**
* Supports **multi-layered security**:

  * AES Encryption
  * QR Key Sharing
  * Steganography

---

## 📈 Future Enhancements

* Cloud storage integration
* End-to-end encrypted messaging
* Multi-user authentication system
* Mobile application support

---

