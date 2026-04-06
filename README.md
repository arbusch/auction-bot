# Ad Bidding Bot (Playtech Internship Assignment)

## Overview

This project implements a Java-based bidding bot for the "Attention Economy Bid" simulation.

The bot participates in real-time auctions for video advertisements and aims to maximize:

> **Total value (points) / Ebucks spent**

while complying with strict performance and protocol constraints.

---

## Strategy

The bot uses a **lightweight heuristic scoring system** to estimate the value of each impression and adjusts bids accordingly.

### 1. Value Estimation

Each bid request is scored based on:

- **Category match**
  - Strong preference for `ASMR`
- **Viewer interests**
  - Bonus if viewer has `ASMR` among interests
- **Subscription status**
  - Subscribed viewers are more valuable
- **Engagement ratio**
  - Based on `commentCount / viewCount`

This produces a simple integer score representing estimated value.

---

### 2. Bidding Logic

The bidding strategy is intentionally simple and efficient:

- **Low-value impressions (score ≤ 2)**
  - Minimal participation (`0–1` ebucks)
  - Avoids wasting budget

- **Higher-value impressions**
  - Bid scaled linearly:
    ```
    maxBid = score * 3
    ```
  - Starting bid:
    ```
    startBid = maxBid / 2
    ```

This ensures:
- Competitive bids on valuable impressions
- Controlled spending on lower-quality ones

---

### 3. Design Principles

- **Efficiency over volume**
  - Focus on high-value impressions rather than winning everything
- **Simplicity**
  - Low-latency (<40ms) guaranteed
- **Stability**
  - Avoid overfitting or aggressive strategies that reduce score

---

## Technical Details

- Language: Java (standard JDK only)
- No external libraries
- Single-threaded (as required)
- Input/output via `stdin` / `stdout`

---

## How to Run

Compile and package:

```bash
rm -rf out bot.jar
mkdir out
javac -d out src/Main.java
jar --create --file bot.jar --main-class Main -C out .