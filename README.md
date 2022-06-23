# Realtime Face Recognizer
The mobile application has been developed with Java programming language and Firebase. The application deveolped for three users: University Admin(handles users and modules records), University Lecturer(handles student attendance either based on face recogntion or checkbox, view attendance), University Student(take attendance based on face recogntion and view attendance). 

[![Build Status](https://travis-ci.org/pillarpond/face-recognizer-android.svg?branch=master)](https://travis-ci.org/pillarpond/face-recognizer-android)

This sample demonstrates realtime face recognition on Android. The project is based on the [FaceNet](https://arxiv.org/abs/1503.03832).

## Inspiration
The project is heavily inspired by
* [FaceNet](https://github.com/davidsandberg/facenet)
* [MediaPipe](https://github.com/google/mediapipe)



## Pre-trained model
from davidsandberg's facenet

| Model name      | LFW accuracy | Training dataset | Architecture |
|-----------------|--------------|------------------|-------------|
| [20180402-114759](https://drive.google.com/open?id=1EXPBSXwTaqrSC0OhUdXNmKSh9qJUQ55-) | 0.9965        | VGGFace2      | [Inception ResNet v1](https://github.com/davidsandberg/facenet/blob/master/src/models/inception_resnet_v1.py) |

from MediaPipe
  * [TFLite model](https://github.com/google/mediapipe/tree/master/mediapipe/models/face_detection_front.tflite)
  * Paper: ["BlazeFace: Sub-millisecond Neural Face Detection on Mobile GPUs"](https://sites.google.com/corp/view/perception-cv4arvr/blazeface)

## License
[Apache License 2.0](./LICENSE)
