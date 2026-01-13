# Taiwan-Stock-App-Kotlin

**TWStock Master** is a high-performance Android app built with **Kotlin** and **MVVM**. It leverages **TWSE OpenAPI** to provide real-time insights into P/E ratios, yields, and market trends. Features include **Jetpack Compose**, **Material Design 3**, **Unit Testing**, **UI Testing**, and **Dark Mode** support for a premium UX.

---

## 🛠 Technical Highlights

* **Architecture**: MVVM + Clean Architecture
* **UI**: XML-based Views for main screens, **Jetpack Compose** for reusable/custom components
* **Async**: Kotlin Coroutines & Flow
* **Networking**: Retrofit2 + OkHttp
* **Design**: Material Design 3, animations, Dark Mode
* **Testing**: Unit / API / UI / Widget tests fully covered

---

## 📊 API Usage

This project integrates official data from the **Taiwan Stock Exchange (TWSE)**:

* **P/E & Yield**: `/exchangeReport/BWIBBU_ALL`
* **Monthly Average**: `/exchangeReport/STOCK_DAY_AVG_ALL`
* **Daily Trading**: `/exchangeReport/STOCK_DAY_ALL`

Base URL:

```
https://openapi.taifex.com.tw
```

---

## 🚀 Getting Started

```bash
git clone https://github.com/kinpo28406-gif/Taiwan-Stock-App-Kotlin.git
```

---

## 🛠 Development Specification (Internal)

### 1. Architecture & Frameworks

* **Language**: Kotlin 1.9+
* **Architecture Pattern**: MVVM + Clean Architecture
* **UI System**:

  * XML (Activities / Fragments)
  * Jetpack Compose (Custom widgets, charts, reusable UI)
* **Async & State**:

  * Kotlin Coroutines
  * StateFlow / Flow

---

### 2. API Layer Specification

* **Networking**: Retrofit2 + OkHttp
* **Serialization**: Moshi / Gson
* **Endpoints**:

  * `/exchangeReport/BWIBBU_ALL` → P/E Ratio, Yield, P/B
  * `/exchangeReport/STOCK_DAY_AVG_ALL` → Monthly Average Prices
  * `/exchangeReport/STOCK_DAY_ALL` → Daily Trading Data

#### API Error Handling

* HTTP status code handling (200 / 4xx / 5xx)
* Network timeout & retry strategy
* Empty or malformed data fallback

---

### 3. UI / UX Requirements

* **Design System**: Material Design 3
* **Themes**:

  * Light / Dark Mode
  * Dynamic color support (Android 12+)
* **Animations**:

  * List item fade-in
  * Smooth scroll transitions
  * Shared element transitions (where applicable)
* **Layout**:

  * Portrait & Landscape support
  * Adaptive layout for tablets

---

### 4. Testing Strategy (Mandatory)

#### 4.1 API Testing

* **Tools**: JUnit5, MockWebServer
* **Scope**:

  * API request correctness
  * Response parsing validation
  * Error / timeout simulation
* **Coverage**:

  * Repository layer
  * DataSource layer

#### 4.2 Unit Testing (Function-Level)

* **Tools**: JUnit5, Mockito / MockK
* **Targets**:

  * ViewModel business logic
  * UseCase / Interactor logic
  * Data transformation & calculation functions
* **Requirements**:

  * No Android framework dependency
  * Deterministic & isolated tests

#### 4.3 UI / Widget Testing

* **View-based UI**:

  * Espresso
  * FragmentScenario
* **Jetpack Compose**:

  * Compose UI Test framework
* **Coverage**:

  * Widget rendering correctness
  * User interaction (click, scroll, input)
  * State change & recomposition behavior

#### 4.4 Integration Testing

* ViewModel ↔ Repository integration
* Fake API + real ViewModel

---

### 5. Quality Standards

* **Code Style**: Kotlin official style guide
* **Architecture Rules**:

  * UI layer must not access data layer directly
  * ViewModel must be UI-framework agnostic
* **Test Coverage Goal**:

  * Unit Tests ≥ 70%
  * Critical business logic ≥ 90%

---

### 6. CI / Automation (Optional but Recommended)

* GitHub Actions
* Automated build & test pipeline
* Lint + Unit Test on every PR

---

## ✅ Summary

This specification **explicitly includes**:

* ✅ API testing
* ✅ Function / business logic testing
* ✅ Widget & UI testing

Ensuring **high reliability, maintainability, and scalability** for long-term development.
