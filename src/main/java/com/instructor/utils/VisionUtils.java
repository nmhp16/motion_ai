package com.instructor.utils;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class VisionUtils {
    static {
        // This requires the OpenCV native library (.so, .dll, or .dylib) to be on the
        // library path
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native OpenCV library not found. Some vision features may be disabled.");
        }
    }

    /**
     * Sample method demonstrating OpenCV Java use: draws a point on a frame
     */
    public void drawKeypoint(Mat frame, double x, double y, String label) {
        Imgproc.circle(frame, new Point(x, y), 5, new Scalar(0, 255, 0), -1);
        Imgproc.putText(frame, label, new Point(x + 10, y + 10),
                Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255, 255, 255), 1);
    }

    /**
     * Reads a video frame using OpenCV
     */
    public Mat readFrame(String videoPath, int frameIndex) {
        VideoCapture capture = new VideoCapture(videoPath);
        Mat frame = new Mat();
        if (capture.isOpened()) {
            capture.set(1, frameIndex); // CV_CAP_PROP_POS_FRAMES = 1
            capture.read(frame);
        }
        capture.release();
        return frame;
    }
}
