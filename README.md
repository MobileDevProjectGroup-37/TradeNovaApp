

![image-removebg-preview](https://github.com/user-attachments/assets/b206567a-70bb-4565-8dd9-3c67fdc2a517)


**TradeNova** is a mobile-first educational trading simulator that helps beginners understand how trading works — without risking real money. The app provides real-time crypto market data, allows users to simulate trades, track performance, and learn by doing in a structured, intuitive environment.

> Built as a student project with potential to evolve into a real EdTech x FinTech startup.

---

## 🚀 Features

- 🔐 Google Sign-In (Firebase Auth)
- 💸 Real-time cryptocurrency prices (Binance API)
- 📉 24-hour change, volume, and percent movement
- 📈 Mini candlestick chart (10-hour close prices)
- 💼 Portfolio management with simulated trading
- 🏆 User leaderboard with ranking system
- 🖌️ Modern UI with Jetpack Compose & Material You
- 🔌 (WIP) WebSocket support for live updates
- ☁️ Firebase Firestore integration for user data
- ⚙️ Dependency Injection via Hilt

---

## 🛠️ Tech Stack

- **Language:** Kotlin  
- **UI:** Jetpack Compose, Material 3 (Material You)  
- **Architecture:** MVVM (Model-View-ViewModel)  
- **Network:** Retrofit, OkHttp, WebSocket, Gson  
- **Firebase:** Auth, Firestore, Storage, Functions  
- **Auth:** Google Sign-In  
- **Charting:** MPAndroidChart  
- **QR Support:** ZXing  
- **Async:** Kotlin Coroutines, Flow  
- **DI:** Dagger Hilt  
- **Navigation:** Jetpack Navigation Compose  
- **Testing:** JUnit, Espresso, Compose Testing  

---

## 📦 Project Structure

<pre>
com.example.traderapp
├── data
│   ├── model           // DTOs and data classes
│   ├── network         // API, WebSocket, session
│   └── repositories    // Crypto & Auth repositories
├── ui
│   ├── screens         // All Compose screen modules
│   ├── components      // Shared UI components
│   └── theme           // Typography, colors, shapes
├── utils               // Helpers (QR, Storage, etc.)
├── scaffold            // App-level UI scaffold
└── viewmodel           // State handling & logic
</pre>

---

## 📲 Getting Started

### Requirements
- Android Studio Hedgehog or newer
- Kotlin 1.9+
- Firebase project with enabled Auth & Firestore
- (Optional) Binance API key for higher limits

### Setup Instructions

1. **Clone the repo**
```bash
git clone https://github.com/MobileDevProjectGroup-37/TradeNovaApp.git
cd TradeNovaApp
```

2. **Add Firebase config**
   - Download `google-services.json` from your Firebase console
   - Place it into the `/app` directory

3. **(Optional)** Add API key to `local.properties` or directly in `build.gradle.kts`:
```kotlin
buildConfigField(
    "String",
    "COINCAP_API_KEY",
    "\"your_api_key_here\""
)
```

4. **Run the app**
   - Use an emulator or physical Android device (min SDK 24)

---

## 🌐 API Details

The app uses **public endpoints from Binance**:
- `/api/v3/exchangeInfo` — metadata
- `/api/v3/ticker/price` — current prices
- `/api/v3/ticker/24hr` — volume & 24h change
- `/api/v3/klines` — OHLC data for mini-charts

To reduce load and avoid throttling, results are cached locally for 15 minutes.

---

## 📚 About the Project

This project was developed as part of a university course in Spring 2025.  
The idea was to create a **realistic, mobile-first trading simulator**.




## 📄 License

This is an academic prototype.  
No official license applied yet — for educational use only.  
Contact us if you wish to contribute, fork, or reuse parts of the code.

---

## 🙌 Contributions

Pull requests and suggestions are welcome.  
For feedback, bugs or questions — please open an issue or contact the team.

# Screenshots
![image-removebg-preview (4)](https://github.com/user-attachments/assets/cedcb3d2-6f58-4bfe-b335-917d4d0828bf)
![image-removebg-preview (3)](https://github.com/user-attachments/assets/5b512831-1341-4190-ad03-e45b896d31ba)
