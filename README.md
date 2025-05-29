# 📒 Address Book API

A high-performance, full-text searchable **Address Book service** built with [Quarkus](https://quarkus.io/).  
It supports inserting, querying, updating, and deleting contacts via REST endpoints — with **substring-based search** using an inverted index.

We currently have two implementations:
- **FullTokenizationImpl**:
  - Uses a full substring search on `name`, `email`, and `phone`
  - Implements an in-memory inverted index for fast lookups
- **KGramAndInvertedIndexingImpl**:
  - Uses K-Gram tokenization for substring search with max substring length of 10
  - Implements an in-memory inverted index for fast lookups
- **HybridImpl**:
  - Combines both partial tokenization and K-Gram indexing for maximum flexibility
  - Uses an in-memory inverted index for fast lookups

---

## 🚀 Features

- Full substring search on `name`, `email`, and `phone`
- Fast in-memory inverted index using all substrings (tokenization)
- REST API with CRUD operations
- Fast performance with support for thousands of contacts
- Quarkus Dev UI support for debugging and live coding

---

| Method   | Endpoint   | Description              |
| -------- |------------| ------------------------ |
| `POST`   | `/create`  | Insert multiple contacts |
| `GET`    | `/search`  | Search contacts by token |
| `PUT`    | `/update`  | Update contact details   |
| `DELETE` | `/delete`  | Delete contacts by ID    |

---

## 📦 Running the Application
### 🔧 Dev Mode (Live Reload)

```bash
./mvnw clean install -DskipTests
./mvnw quarkus:dev
