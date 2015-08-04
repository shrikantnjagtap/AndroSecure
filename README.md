# AndroSecure

Android App - Intrusion detection system which on detecting an unauthorized intruder trying to access the car,
signals the owner by sending sms, email the image of the face of the intruder, and also evokes an alarming sound.
Technologies - Android, OpenCV

The Android system is divided into following activities with each activity
performing a well defined function.
-----------------------------------------------------------------------------------------
Sr.No  |    Activity           |       Operation                                            
-----------------------------------------------------------------------------------------
  1.   |    Controller         |       Controls and coordinates all other activities.
  2.   |    VoiceActivity      |       Converts speech to text.
  3.   |    FaceDetection      |       Detects face and captures image.
  4.   |    SmsActivity        |       Sends SMS to given mobile number.
  5.   |    AlarmActivity      |       Sound alarm till user stops.
  6.   |    DbManager          |       Manages database related calls.
  7.   |    AdminActivity      |       Manage Credentials.
-----------------------------------------------------------------------------------------  
