
var UTILS = utilsModule; 
var StateEnum = UTILS.SystemState;

var state = StateEnum.ST_INIT;

//------------------------------------------------------------------------------
//modifyElementsAccordingToState 
//------------------------------------------------------------------------------
function modifyElementsAccordingToState(state) {
    switch(state) {
        case StateEnum.ST_INIT:      
                                $("#alertMessageDiv").hide();
                                break;
        case StateEnum.ST_ENCRYPT_MESSAGE_REQ_SENT:
                                break;
        case StateEnum.ST_ENCRYPT_MESSAGE_REQ_RECEIVED:
                                break;
        case StateEnum.ST_ENCRYPT_MESSAGE_REQ_ERROR:
                                break; 
        case StateEnum.ST_DECRYPT_MESSAGE_REQ_SENT:
                                $('#decryptedMessage').val("");
                                break;
        case StateEnum.ST_DECRYPT_MESSAGE_REQ_RECEIVED:
                                break;
        case StateEnum.ST_DECRYPT_MESSAGE_REQ_ERROR:
                                break; 
        case StateEnum.ST_ERROR:
        default:
                                break;                    
    }
}

//------------------------------------------------------------------------------
//wait for response process 
//------------------------------------------------------------------------------
const SERVER_QUERY_TIMEOUT = 200000;

function responseWaitingTimeoutError() {        
    var mess = 'Server timeout error.';
    UTILS.showAlert(mess);
    mess = 'Server timeout error. Connection was closed.';
    UTILS.showAlert(mess);
    asyncRESTRequestStop();
    UTILS.responseWaitingStop(); 
    if(state === StateEnum.ST_ENCRYPT_MESSAGE_REQ_SENT) {
        state = StateEnum.ST_ENCRYPT_MESSAGE_REQ_ERROR;
    }    
    modifyElementsAccordingToState(state);
    console.log(mess);
    UTILS.setConnectedStatus(mess);    
}
//wait for response process end ------------------------------------------------

//production list query end ----------------------------------------------------

String.prototype.isNumber = function() { 
    return /^\d+$/.test(this);
};

//------------------------------------------------------------------------------
//answer handler
//------------------------------------------------------------------------------
function encryptMessageResponseJsonHandle(response) {
   var packetType = JSON.parse(response).packetType;
    $( "#gerResultButtonSpinner" ).hide();
    if(packetType === 'EncryptMessageResponse') {
        UTILS.setConnectedStatus('EncryptMessageResponse received successfully.');
        state = StateEnum.ST_ENCRYPT_MESSAGE_REQ_RECEIVED; 
        $("#encodedMessage").val(JSON.parse(response).encryptedMessage);
    } else if(packetType === 'ErrorPacket') {
        var errors = JSON.parse(response).message;
        UTILS.showAlert(errors);
        UTILS.setConnectedStatus('EncryptMessageResponse has errors: ' + errors);
        state = StateEnum.ST_ENCRYPT_MESSAGE_REQ_ERROR;
    } 
    UTILS.responseWaitingStop();
    modifyElementsAccordingToState(state);
}

function decryptMessageResponseJsonHandle(response) {
   var packetType = JSON.parse(response).packetType; 
    $( "#gerDecryptResultButtonSpinner" ).hide();
    if(packetType === 'DecryptMessageResponse') {
        UTILS.setConnectedStatus('DecryptMessageResponse received successfully.');
        state = StateEnum.ST_DECRYPT_MESSAGE_REQ_RECEIVED; 
        $("#decryptedMessage").val(JSON.parse(response).decryptedMessage);
    } else if(packetType === 'ErrorPacket') {
        var errors = JSON.parse(response).message;
        UTILS.showAlert(errors);
        UTILS.setConnectedStatus('DecryptMessageResponse has errors: ' + errors);
        state = StateEnum.ST_DECRYPT_MESSAGE_REQ_ERROR;
    } 
    UTILS.responseWaitingStop();
    modifyElementsAccordingToState(state);
}

//------------------------------------------------------------------------------
//REST
//------------------------------------------------------------------------------
function encryptMessage() {
    var message = $("#sourceMessageInput").val();
    var numberA = $("#numberAInput").val();
    var numberB = $("#numberBInput").val();
    
    $( "#gerResultButtonSpinner" ).show();
    
    state = StateEnum.ST_ENCRYPT_MESSAGE_REQ_SENT;
    modifyElementsAccordingToState(state); 
    UTILS.responseWaitingStart(SERVER_QUERY_TIMEOUT);
    asyncRESTRequestHandler({'cmd': 'encryptMessage',
                                'message' : message,
                                'numberA' : numberA,
                                'numberB' : numberB});
    UTILS.setConnectedStatus("EncryptMessageRequest sent");    
}

function decryptMessage() {
    var message = $("#encryptedMessageInput").val();
    var numberA = $("#encryptedNumberAInput").val();
    var numberB = $("#encryptedNumberBInput").val();
    
    $( "#gerDecryptResultButtonSpinner" ).show();
    
    state = StateEnum.ST_DECRYPT_MESSAGE_REQ_SENT;
    modifyElementsAccordingToState(state); 
    UTILS.responseWaitingStart(SERVER_QUERY_TIMEOUT);
    asyncRESTRequestHandler({'cmd': 'decryptMessage',
                                'message' : message,
                                'numberA' : numberA,
                                'numberB' : numberB});
    UTILS.setConnectedStatus("EncryptMessageRequest sent");    
}

var xhr = null;
function asyncRESTRequestHandler(data) {
    var json = null;
    switch (data.cmd) {
        case 'encryptMessage':
            xhr = new XMLHttpRequest();
            xhr.open('POST', '/rest/lab2/encrypt', true);
            xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
            xhr.onload = function () {
                console.log(xhr.responseText);
                encryptMessageResponseJsonHandle(xhr.responseText);
            };    
            xhr.onabort = function () {
                UTILS.setConnectedStatus('EncryptMessageRequest was aborted.');
            };
            json = JSON.stringify({packetType:"EncryptMessageRequest",
                                    'message' : data.message,
                                    'numberA' : data.numberA,
                                    'numberB' : data.numberB});
            console.log(json);
            xhr.send(json);
            break; 
        case 'decryptMessage':
            xhr = new XMLHttpRequest();
            xhr.open('POST', '/rest/lab2/decrypt', true);
            xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
            xhr.onload = function () {
                console.log(xhr.responseText);
                decryptMessageResponseJsonHandle(xhr.responseText);
            };    
            xhr.onabort = function () {
                UTILS.setConnectedStatus('DecryptMessageRequest was aborted.');
            };
            json = JSON.stringify({packetType:"DecryptMessageRequest",
                                    'message' : data.message,
                                    'numberA' : data.numberA,
                                    'numberB' : data.numberB});
            console.log(json);
            xhr.send(json);
            break; 
        case 'stop':
            if(xhr !== null) {
                xhr.abort();
            }
            break;
        default:
            console.log('Unknown command: ' + data.cmd);
    };
}

function asyncRESTRequestStop() {
    asyncRESTRequestHandler({'cmd': 'stop'});
}

// end REST --------------------------------------------------------------------

$(document).ready(function () {    
    UTILS.setResponseWaitingTimeoutErrorHandler(responseWaitingTimeoutError);
    
    $("#alertMessageDiv").hide();
    
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    
    state = StateEnum.ST_INIT;
    modifyElementsAccordingToState(state);
    
    $( "#gerResultButton" ).click(function() {
        if(UTILS.isAnotherProcessRunning()) { return; };
        encryptMessage();
    });
    
    $( "#gerDecryptResultButton" ).click(function() {
        if(UTILS.isAnotherProcessRunning()) { return; };
        decryptMessage();
    });
    
});

