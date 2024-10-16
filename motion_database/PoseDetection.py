import cv2
import mediapipe as mp
import numpy as np
import matplotlib.pyplot as plt

# Initialize MediaPipe Pose
mp_pose = mp.solutions.pose
mp_drawing = mp.solutions.drawing_utils

class PoseEstimationService:
    def __init__(self, video_path):
        self.pose = mp_pose.Pose()
        self.keypoints_data = self.initialize_keypoints_data()
        self.frame_counter = 0
        self.video_path = video_path

    def initialize_keypoints_data(self):
        return {
            "nose": [], "left_eye": [], "right_eye": [], "left_ear": [], "right_ear": [],
            "shoulder_left": [], "shoulder_right": [], "elbow_left": [], "elbow_right": [],
            "wrist_left": [], "wrist_right": [], "hip_left": [], "hip_right": [],
            "knee_left": [], "knee_right": [], "ankle_left": [], "ankle_right": []
        }

    def start_video_capture(self):
        cap = cv2.VideoCapture(self.video_path)

        frame_width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
        frame_height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))

        # Define the codec and create VideoWriter object for keypoints-only video
        fourcc = cv2.VideoWriter_fourcc(*'XVID')
        out = cv2.VideoWriter('keypoints_line_video.avi', fourcc, 20.0, (frame_width, frame_height))

        while cap.isOpened():
            ret, frame = cap.read()
            if not ret:
                print("End of video or failed to read frame.")
                break

            # Create a blank frame for drawing keypoints
            blank_frame = np.zeros((frame_height, frame_width, 3), dtype=np.uint8)
            image = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
            image.flags.writeable = False

            # Pose detection
            results = self.pose.process(image)

            if results.pose_landmarks:
                # Draw keypoints and connections on the blank frame (keypoints only, no original frame)
                mp_drawing.draw_landmarks(
                    blank_frame, 
                    results.pose_landmarks, 
                    mp_pose.POSE_CONNECTIONS,
                    mp_drawing.DrawingSpec(color=(255, 255, 255), thickness=2, circle_radius=2),  # Keypoints
                    mp_drawing.DrawingSpec(color=(0, 255, 0), thickness=2, circle_radius=2)  # Connections
                )
                self.extract_pose_keypoints(results.pose_landmarks.landmark)

            # Write the keypoints frame (blank_frame) to the output video file
            out.write(blank_frame)

            # Display the keypoints-only frame
            cv2.imshow('Keypoints Line Video', blank_frame)

            if cv2.waitKey(5) & 0xFF == 27:  # Press 'ESC' to exit
                break

            self.frame_counter += 1  # Increment the frame counter

        # Release resources
        cap.release()
        out.release()
        cv2.destroyAllWindows()

        self.save_keypoints_data()  # Save keypoints data to a text file
        self.plot_keypoints_with_distance()  # Call the plot function after capturing

    def extract_pose_keypoints(self, landmarks):
        for idx, landmark in enumerate(landmarks):
            # Map the index to body parts
            keypoint_map = {
                mp_pose.PoseLandmark.NOSE.value: "nose",
                mp_pose.PoseLandmark.LEFT_EYE.value: "left_eye",
                mp_pose.PoseLandmark.RIGHT_EYE.value: "right_eye",
                mp_pose.PoseLandmark.LEFT_EAR.value: "left_ear",
                mp_pose.PoseLandmark.RIGHT_EAR.value: "right_ear",
                mp_pose.PoseLandmark.LEFT_SHOULDER.value: "shoulder_left",
                mp_pose.PoseLandmark.RIGHT_SHOULDER.value: "shoulder_right",
                mp_pose.PoseLandmark.LEFT_ELBOW.value: "elbow_left",
                mp_pose.PoseLandmark.RIGHT_ELBOW.value: "elbow_right",
                mp_pose.PoseLandmark.LEFT_WRIST.value: "wrist_left",
                mp_pose.PoseLandmark.RIGHT_WRIST.value: "wrist_right",
                mp_pose.PoseLandmark.LEFT_HIP.value: "hip_left",
                mp_pose.PoseLandmark.RIGHT_HIP.value: "hip_right",
                mp_pose.PoseLandmark.LEFT_KNEE.value: "knee_left",
                mp_pose.PoseLandmark.RIGHT_KNEE.value: "knee_right",
                mp_pose.PoseLandmark.LEFT_ANKLE.value: "ankle_left",
                mp_pose.PoseLandmark.RIGHT_ANKLE.value: "ankle_right"
            }

            if idx in keypoint_map:
                self.keypoints_data[keypoint_map[idx]].append([self.frame_counter, landmark.x, landmark.y, landmark.z])

    def save_keypoints_data(self):
        # Save the keypoints data to a .txt file
        with open('keypoints_data.txt', 'w') as f:
            for keypoint, positions in self.keypoints_data.items():
                if positions:
                    f.write(f"{keypoint}:\n")
                    for pos in positions:
                        f.write(f"  Frame {pos[0]}: x={pos[1]:.4f}, y={pos[2]:.4f}, z={pos[3]:.4f}\n")
                    f.write("\n")
        print("Keypoints data saved to keypoints_data.txt")

    def plot_keypoints_with_distance(self):
        plt.figure(figsize=(12, 6))
        for keypoint, positions in self.keypoints_data.items():
            if positions:
                time_vals = [pos[0] for pos in positions]
                x_vals = [pos[1] for pos in positions]
                y_vals = [pos[2] for pos in positions]
                distance_vals = [np.sqrt(x ** 2 + y ** 2) for x, y in zip(x_vals, y_vals)]

                plt.plot(time_vals, distance_vals, label=f'{keypoint} Distance', linestyle=':', marker='x')

        plt.title('Keypoint Coordinates and Distance Over Time')
        plt.xlabel('Frame Index (Time)')
        plt.ylabel('Distance from Origin')
        plt.grid()
        plt.legend()
        plt.tight_layout()
        plt.savefig('keypoints_coordinates_distance_plot.png')  # Save the plot
        plt.show()  # Replace with plt.close() if you don't want to display the plot

# Start the video capture from a video file
video_file_path = 'motion_database/Beginner.mp4'  # Specify the video file path
pose_service = PoseEstimationService(video_file_path)

# Start processing the video file
pose_service.start_video_capture()
