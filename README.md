# direkuta
Direkuta is a video/audio stream event handler for the EvoStream media server.

## Configuration
EvoStream must be configured to send events to direkuta. 
 * Edit `/etc/evostreamms/config.lua` and change the `url` parameter in `eventLogger->sinks` to point to your locally running copy of direkuta.
   * Example: `url="http://127.0.0.1:1111/v1/events/process",`
