package com.hello.direkuta.models;

/**
 * Created by jnorgan on 3/16/17.
 */
public enum EventType {
    HLSChuckClosed,
    hlsChildPlaylistUpdated,
    inStreamCreated,
    outStreamCreated,
    inStreamClosed,
    outStreamClosed,
    streamCreated,
    streamClosed,
    inStreamCodecsUpdated,
    outStreamCodecsUpdated,
    cliRequest,
    cliResponse,
    protocolRegisteredToApp,
    protocolUnregisteredFromApp,
    audioFeedStopped,
    serverStopping,
    serverStarted,
    applicationStart
}
