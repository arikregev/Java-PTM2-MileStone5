package view;

public class SimPaths {
	String[] paths = {
			"/instrumentation/airspeed-indicator/indicated-speed-kt",
			"/instrumentation/altimeter/indicated-altitude-ft", 
			"/instrumentation/altimeter/pressure-alt-ft",
			"/instrumentation/attitude-indicator/indicated-pitch-deg",
			"/instrumentation/attitude-indicator/indicated-roll-deg",
			"/instrumentation/attitude-indicator/internal-pitch-deg",
			"/instrumentation/attitude-indicator/internal-roll-deg",
			"/instrumentation/encoder/indicated-altitude-ft",
			"/instrumentation/encoder/pressure-alt-ft",
			"/instrumentation/gps/indicated-altitude-ft",
			"/instrumentation/gps/indicated-ground-speed-kt",
			"/instrumentation/gps/indicated-vertical-speed",
			"/instrumentation/heading-indicator/indicated-heading-deg",//indicated-heading-deg
			"/instrumentation/magnetic-compass/indicated-heading-deg",
			"/instrumentation/slip-skid-ball/indicated-slip-skid",
			"/instrumentation/turn-indicator/indicated-turn-rate",
			"/instrumentation/vertical-speed-indicator/indicated-speed-fpm",
			"/controls/flight/aileron",
			"/controls/flight/elevator",
			"/controls/flight/rudder",
			"/controls/flight/flaps",
			"/controls/engines/current-engine/throttle",
			"/engines/engine/rpm",
			"/controls/flight/speedbrake",
			"/instrumentation/heading-indicator/offset-deg",
			"/instrumentation/gps/indicated-latitude-deg",
			"/instrumentation/gps/indicated-longitude-deg"
			};
	
	public String[] getPaths() {
		return this.paths;
	}
}
