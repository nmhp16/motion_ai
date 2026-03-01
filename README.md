# Motion AI: Kinematic Intelligence Platform
**Enterprise-Grade Movement Telemetry & Real-Time Biometric Feedback**

## Executive Summary
Motion AI is a state-of-the-art **Kinematic Intelligence Platform** engineered to provide deterministic, real-time feedback for elite athletes, performers, and clinical physical therapy applications. By synthesizing computer vision at the edge with an event-driven serverless cloud architecture, the system facilitates high-fidelity comparisons between user biomechanics and professional benchmarks with sub-100ms end-to-end latency.

## Technical Key Achievements
*   **Scalable Serverless Compute Orchestration**: Engineered a heavy-lifting Java-native pipeline powered by **AWS Lambda**, achieving asynchronous processing of high-frequency movement telemetry.
*   **High-Fidelity Telemetry Ingestion**: Developed a hybrid capture system utilizing **OpenCV** and **MediaPipe** for skeletal coordinate extraction, leveraging **Amazon S3** as a high-throughput metadata buffer for rapid state analysis.
*   **High-Performance Data Persistence**: Designed a structured **Amazon DynamoDB** schema with optimized Global Secondary Indexes (GSIs) to manage complex user performance metrics, enabling millisecond-latency queries for Longitudinal Performance Tracking.
*   **Proprietary Kinematic Algorithmic Engine**: Implemented a sophisticated **Dynamic Time Warping (DTW)** engine for multi-dimensional sequence alignment. This engine utilizes a recursive cost-matrix optimization to neutralize temporal variances (speed fluctuations) and morphological differences (body types) during real-time movement execution, ensuring a normalized performance score.

## System Architecture & Distributed Orchestration

### 1. Hybrid Edge-to-Cloud Telemetry Ingestion
Motion AI implements a **Latency-Sensitive Telemetry Dispatcher** where skeletal coordinates are extracted locally via a vectorized CV pipeline and asynchronously pushed to the cloud for heavy-lifting state alignment.
*   **Vectorized Edge Extraction**: Utilizes heavily optimized native bindings for OpenCV and MediaPipe to ensure zero-overhead frame processing.
*   **Asynchronous Message Distributon**: Skeletal metadata is batched and dispatched to **Amazon S3** acting as a high-throughput event buffer.
*   **Serverless Orchestration**: Native **AWS Lambda** triggers propagate kinematic events to the evaluation engine.

### 2. Proprietary Kinematic Evaluation Engine
The evaluation layer synthesizes advanced signal processing with biometric data science:
*   **Morphological Invariance**: Torso-relative normalization ensures cross-subject comparability, effectively decoupling skeletal analysis from individual body dimensions.
*   **Bilateral Signal Filtering**: Implements Cascaded Moving Averages and Linear Interpolation to neutralize sensor jitter and atmospheric noise in coordinate telemetry.
*   **Multi-Weighted Dynamic Time Warping (DTW)**: A recursive cost-matrix optimization engine that aligns temporal sequences, accounting for non-linear speed variations between subjects.

## Engineering Stack
*   **Core Orchestration**: Java 21 LTS (JavaFX High-Performance UI)
*   **Cloud Ecosystem**: AWS (Lambda, S3, DynamoDB, IAM, CloudWatch)
*   **Kinematic Extraction**: MediaPipe, OpenCV (Native Bindings)
*   **Mathematical Logic**: Dynamic Time Warping (DTW), Linear Regression, Vector Calculus

## Getting Started

### Prerequisites
*   **Java 21+**: Optimized for modern JVM performance (e.g., Amazon Corretto 21).
    *   *Note: Ensure `JAVA_HOME` is set. On macOS: `export JAVA_HOME=$(/usr/libexec/java_home)`*
*   **Maven 3.8+**: Project orchestration and dependency lifecycle.
*   **AWS CLI**: Configured for S3, Lambda, and DynamoDB access.
*   **Python 3.10+**: Required for edge pose estimation.
*   **OpenCV Native Binaries**: Required for edge vision pre-processing.

### Environmental Configuration & Provisioning
Initialize the local environment and resolve distributed dependencies:

1. **Configure AWS Credentials**:
   Create a `.env` file in the project root:
   ```env
   AWS_ACCESS_KEY_ID="your_access_key"
   AWS_SECRET_ACCESS_KEY="your_secret_key"
   AWS_REGION="your_region"
   ```

2. **Setup Python Virtual Environment**:
   The application expects a `venv` directory in the root for consistent execution.
   ```bash
   python3 -m venv venv
   source venv/bin/activate
   pip install -r requirements.txt
   ```

3. **Build the Java Engine**:
   ```bash
   mvn clean install
   ```

### Execution & Operation
Launch the high-performance JavaFX Telemetry Interface:
```bash
# export JAVA_HOME=$(/usr/libexec/java_home) # Ensure Java 21 is active
mvn javafx:run
```

#### Operating the Capture System:
*   **Start**: Click the "Start Recording" button. The system will pre-warm the MediaPipe models (approx. 2-3 seconds) before the camera window appears.
*   **Stop**: 
    *   **Keyboard**: Press the **`ESC`** key while the camera window is focused.
    *   **Voice**: Say the word **"Stop"** clearly (requires microphone access).
*   **Data Output**: Upon stopping, the system generates a video (`user.avi`) and a skeletal telemetry file (`user.txt`) for cloud synchronization.


