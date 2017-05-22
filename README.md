# Beep-Box
Android Metronome. Beep beep.

### Screenshots
![Screenshot](/screenshots/beepbox-main.jpeg)
![Screenshot](/screenshots/beepbox-timesignature.jpeg)

### How to Use
Beep Box is simple to use. It has a clear, concise, and responsive design.

Hit the START button to toggle the metronome on, and STOP to toggle off. 

Tap or hold the "-" or "+" buttons to decrease or increase the beats per minute, or slide the seekbar (slider).

Tap the TAP button to a beat to set a tempo to match an outside source, like a song on the radio.

Select a time signature from the dropdown to designate the amount of beats per measure.  An accent will be applied to the first beat of each measure.

### About
Beep Box uses a handler and a runnable to call a sound at the given interval.  This persists while the app is no longer in the foreground.  Another solution could be using a service. SoundPool is used but AudioTrack could provide a steadier beat.

### Download
Google Play Store link coming soon...
