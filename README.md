# 📒 Address Book API

A high-performance, full-text searchable **Address Book service** built with [Quarkus](https://quarkus.io/).  
It supports inserting, querying, updating, and deleting contacts via REST endpoints — with **substring-based search** using an inverted index.

We currently have three implementations:
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

## Endpoints

| Method   | Endpoint   | Description              |
| -------- |------------| ------------------------ |
| `POST`   | `/create`  | Insert multiple contacts |
| `GET`    | `/search`  | Search contacts by token |
| `PUT`    | `/update`  | Update contact details   |
| `DELETE` | `/delete`  | Delete contacts by ID    |

---

## 📊 Performance Comparison
| **Criterion**                       | **KGramAndInvertedIndexImpl**                           | **KGramTokenizationImpl**                                 |
| ----------------------------------- |---------------------------------------------------------|-----------------------------------------------------------|
| **Primary Goal**                    | Constant-time (O(1)) search and update                  | Scalable, high-precision substring search                 |
| **Search Time Complexity**          | ✅ **O(1)** via perfect hash or direct ID-keyed access   | ⚠ **O(m)** where `m` is n-gram/token matches (still fast) |
| **Update Complexity**               | ⚠ Realistically **O(n)** or worse if reindexing         | ⚠ **O(n)** due to n-gram/token regeneration               |
| **Insertion Complexity**            | ⚠ High if indexing all substrings                       | ⚠ Moderate (limited n-grams and token splits)             |
| **Memory Usage**                    | 🔺 Very High (needs complete reverse index for O(1))    | ⚖ Moderate (bounded n-grams and token-based)              |
| **Precision (Substring match)**     | ✅ High (all substrings known)                           | ✅ High (ngrams + token split + fallback logic)            |
| **Partial/Fuzzy Matching**          | ❌ Not tolerant to typos unless fuzzy hashed             | ✅ Some tolerance via small n-grams                        |
| **Scalability (Millions of users)** | ⚠ Poor (massive memory growth with more substrings)     | ✅ Better controlled growth via smart indexing             |
| **Best Use Case**                   | Near-instant retrieval by exact known keys              | Real-world search queries from partial/ambiguous input    |

---

## 📦 Running the Application
### 🔧 Dev Mode (Live Reload)

```bash
./mvnw clean install -DskipTests
./mvnw quarkus:dev


![image](https://github.com/user-attachments/assets/442b9b6e-2cb6-4e51-91f0-3528e0fe1597)
