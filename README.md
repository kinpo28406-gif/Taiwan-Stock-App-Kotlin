# Taiwan-Stock-App-Kotlin

**TWStock Master** is a high-performance Android app built with **Kotlin** and **MVVM**. It leverages **TWSE OpenAPI** to provide real-time insights into P/E ratios, yields, and market trends. Features include **Jetpack Compose** components, **Material Design** animations, **Unit Testing**, and **Dark Mode** support for a premium UX.

---

## 🛠 Technical Highlights
* **Architecture**: MVVM (Model-View-ViewModel) + Clean Architecture principles.
* **UI**: Custom XML layouts combined with **Jetpack Compose** components.
* **Networking**: Retrofit2 & Coroutines for asynchronous API handling.
* **Design**: Adheres to **Material Design** with smooth transitions and **Dark Mode** support.
* **Testing**: Includes **Unit Tests** for core business logic.

## 📊 API Usage
This project integrates official data from the **Taiwan Stock Exchange (TWSE)**:
* **P/E & Yields**: `/exchangeReport/BWIBBU_ALL`
* **Monthly Averages**: `/exchangeReport/STOCK_DAY_AVG_ALL`
* **Daily Trading**: `/exchangeReport/STOCK_DAY_ALL`

## 🚀 Getting Started
1. Clone the repository:
   ```bash
   git clone [https://github.com/kinpo28406-gif/Taiwan-Stock-App-Kotlin.git](https://github.com/kinpo28406-gif/Taiwan-Stock-App-Kotlin.git)