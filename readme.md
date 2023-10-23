# CryptoMiner

This is an enhanced version of the HashingClient provided in the cryptocurrency course at DHBW.

It features:
- Multithreading
- regular updates on currently active hashes and difficulty
- advanced hash selection
- improved stability

## How to use

Pull this repository and build with Maven.

Insert your name into the ```name```-field at the top of the ```Main```-class

## Tweaking

There a some options that can be altered

### Multithreading

The program will start threads for all but one available logical core. You can adjust this by changing the condition of the for-loop in the ```Main```-class.

### Updating active hashes and difficulty

The program will update the currently active hashes, where it chooses which hash trying to append every fifth of the time, at the top of the website. You can adjust the updating interval by changing the ```Thread.sleep``` statement in the ```Watcher```-class

### Advanced hash selection

The program, by default will choose the hash with the highest length. If there are multiple hashes at highest lengt, where one was created by someone from B4 (indicated by the name ending in ```-B4```), it will prefer a hash provided by someone from B4.
The behaviour can be altered by increasing the ```aggressiveness-field``` at the top of the ```HashClient```-class. If set to some value ```n``` the program can choose a hash of your own group even if it is ```n``` blocks behind the currently best hash.

### Logging

You can change the detail of logging. To do this use the static function ```setLevel``` from the ```Logger```-class. There are 3 logging levels:
- ```Logger.MINIMAL```: very minimal logging, shows only if a hash is found
- ```Logger.INFO```: Shows some more info e.g. if a new best hash is found or when the Watcher runs
- ```Logger.DEBUG```: Shows everything
