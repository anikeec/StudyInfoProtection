
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
                                $("#publicKeyTextArea").empty();
                                $("#publicKeyDetailedTextArea").empty();
                                $("#privateKeyTextArea").empty(); 
                                $("#signTextArea").empty(); 
                                break;
        case StateEnum.ST_SIGN_MESSAGE_REQ_SENT:
                                $("#publicKeyTextArea").empty(); 
                                $("#publicKeyDetailedTextArea").empty();
                                $("#privateKeyTextArea").empty(); 
                                $("#signTextArea").empty(); 
                                break;
        case StateEnum.ST_SIGN_MESSAGE_REQ_RECEIVED:
                                break;
        case StateEnum.ST_SIGN_MESSAGE_REQ_ERROR:
                                $("#publicKeyTextArea").empty(); 
                                $("#publicKeyDetailedTextArea").empty();
                                $("#privateKeyTextArea").empty(); 
                                $("#signTextArea").empty();
                                break; 
        case StateEnum.ST_CHECK_SIGNED_MESSAGE_REQ_SENT:
                                $('#decryptedMessage').val("");
                                break;
        case StateEnum.ST_CHECK_SIGNED_MESSAGE_REQ_RECEIVED:
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
    if(state === StateEnum.ST_SIGN_MESSAGE_REQ_SENT) {
        state = StateEnum.ST_SIGN_MESSAGE_REQ_ERROR;
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
function signMessageResponseJsonHandle(response) {
   var packetType = JSON.parse(response).packetType;
    $( "#addSignatureButtonSpinner" ).hide();
    if(packetType === 'SignMessageResponse') {
        UTILS.setConnectedStatus('SignMessageResponse received successfully.');
        state = StateEnum.ST_SIGN_MESSAGE_REQ_RECEIVED; 
        $("#publicKeyTextArea").empty();
        $("#publicKeyTextArea").append(JSON.parse(response).publicKey);
        $("#publicKeyDetailedTextArea").empty();
        $("#publicKeyDetailedTextArea").append(JSON.parse(response).publicKeyDetailed);
        $("#privateKeyTextArea").empty();
        $("#privateKeyTextArea").append(JSON.parse(response).privateKey);
        $("#signTextArea").empty();
        $("#signTextArea").append(JSON.parse(response).signedMessage);
    } else if(packetType === 'ErrorPacket') {
        var errors = JSON.parse(response).message;
        UTILS.showAlert(errors);
        UTILS.setConnectedStatus('SignMessageResponse has errors: ' + errors);
        state = StateEnum.ST_SIGN_MESSAGE_REQ_ERROR;
    } 
    UTILS.responseWaitingStop();
    modifyElementsAccordingToState(state);
}

function checkSignedMessageResponseJsonHandle(response) {
   var packetType = JSON.parse(response).packetType; 
    $( "#checkSignatureButtonSpinner" ).hide();
    if(packetType === 'CheckSignedMessageResponse') {
        UTILS.setConnectedStatus('CheckSignedMessageResponse received successfully.');
        state = StateEnum.ST_CHECK_SIGNED_MESSAGE_REQ_RECEIVED; 
        $("#signatureTextArea").empty();
        $("#signatureTextArea").append(JSON.parse(response).result);     
    } else if(packetType === 'ErrorPacket') {
        var errors = JSON.parse(response).message;
        UTILS.showAlert(errors);
        UTILS.setConnectedStatus('CheckSignedMessageResponse has errors: ' + errors);
        state = StateEnum.ST_CHECK_SIGNED_MESSAGE_REQ_ERROR;
    } 
    UTILS.responseWaitingStop();
    modifyElementsAccordingToState(state);
}

//------------------------------------------------------------------------------
//REST
//------------------------------------------------------------------------------
function signMessage() {
    var message = $("#sourceMessageInput").val();
    var keylength = $("#keyLengthInput").val();
    
    $( "#addSignatureButtonSpinner" ).show();
    
    state = StateEnum.ST_SIGN_MESSAGE_REQ_SENT;
    modifyElementsAccordingToState(state); 
    UTILS.responseWaitingStart(SERVER_QUERY_TIMEOUT);
    asyncRESTRequestHandler({'cmd': 'signMessage',
                                'message' : message,
                                'keylength' : keylength});
    UTILS.setConnectedStatus("SignMessageRequest sent");    
}

function checkSignedMessage() {
    var message = $("#signedMessageInput").val();
    var sign = $("#signForCheckingTextArea").val();
    var publicKey = $("#publicKeyForCheckingTextArea").val();
    
    $( "#checkSignatureButtonSpinner" ).show();
    
    state = StateEnum.ST_CHECK_SIGNED_MESSAGE_REQ_SENT;
    modifyElementsAccordingToState(state); 
    UTILS.responseWaitingStart(SERVER_QUERY_TIMEOUT);
    asyncRESTRequestHandler({'cmd': 'checkSignedMessage',
                                'message' : message,
                                'sign' : sign,
                                'publicKey' : publicKey});
    UTILS.setConnectedStatus("CheckSignedMessage sent");    
}

var xhr = null;
function asyncRESTRequestHandler(data) {
    var json = null;
    switch (data.cmd) {
        case 'signMessage':
            xhr = new XMLHttpRequest();
            xhr.open('POST', '/rest/lab4/sign', true);
            xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
            xhr.onload = function () {
                console.log(xhr.responseText);
                signMessageResponseJsonHandle(xhr.responseText);
            };    
            xhr.onabort = function () {
                UTILS.setConnectedStatus('SignMessageRequest was aborted.');
            };
            json = JSON.stringify({packetType:"SignMessageRequest",
                                    'message' : data.message,
                                    'keylength' : data.keylength});
            console.log(json);
            xhr.send(json);
            break; 
        case 'checkSignedMessage':
            xhr = new XMLHttpRequest();
            xhr.open('POST', '/rest/lab4/checksigned', true);
            xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
            xhr.onload = function () {
                console.log(xhr.responseText);
                checkSignedMessageResponseJsonHandle(xhr.responseText);
            };    
            xhr.onabort = function () {
                UTILS.setConnectedStatus('CheckSignedMessageRequest was aborted.');
            };
            json = JSON.stringify({packetType:"CheckSignedMessageRequest",
                                    'message' : data.message,
                                    'sign' : data.sign,
                                    'publicKey' : data.publicKey});
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
    
    $( "#addSignatureButton" ).click(function() {
        if(UTILS.isAnotherProcessRunning()) { return; };
        signMessage();
    });
    
    $( "#checkSignatureButton" ).click(function() {
        if(UTILS.isAnotherProcessRunning()) { return; };
        checkSignedMessage();
    });
    
});

