# Jarvis XLine Android App

This project is a starter Android app for a local chatbot named **Jarvis**.

## What it includes
- Compose chat UI + settings screen.
- Fixed, non-editable system prompt declaring:
  - runtime technique is **XLine** (created by Vihaan),
  - model identity is **Jarvis**,
  - base model source is **HuggingFaceTB/SmolLM2-135M-Instruct**.
- Hugging Face model downloader for `model.safetensors`.
- Navigator path setting for loading `navigator_weights.pt` style files.
- App API key generation and regeneration in Settings.
- GitHub Actions workflow to build and publish a downloadable APK artifact.

## Important note on local inference runtime
This scaffold wires the app flow and XLine configuration behavior, but the actual on-device LLM runtime binding (GGUF/ONNX/TFLite/ExecuTorch/etc.) must still be integrated for true token-by-token model execution. The placeholder `XLineEngine` demonstrates how navigator weights are loaded for inference-only sessions.

## Build locally
Use **JDK 17** and **Gradle 8.7+**:
```bash
gradle :app:assembleDebug
```

## Download APK from GitHub Actions
1. Push to GitHub.
2. Open **Actions** → **Build Android APK**.
3. Run workflow (or use push-triggered run).
4. Download artifact: `jarvis-xline-debug-apk`.
