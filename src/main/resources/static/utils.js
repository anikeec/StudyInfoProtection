;var utilsModule = (function() {
    
    var UTILS = {};
    
    var elementsModifier = null;
    var responseWaitingTimeoutErrorHandler = null;

    function setElementsModifier(modifier) {
        elementsModifier = modifier;
    }

    function setResponseWaitingTimeoutErrorHandler(handler) {
        responseWaitingTimeoutErrorHandler = handler;
    }
    
    
    var SystemState = {
                "ST_INIT":                              0, 
                "ST_ERROR":                             1,
                
                "ST_CARD_INFO_REQ_SENT":                2, 
                "ST_CARD_CHECKED":                      3, 
                "ST_CARD_INFO_RECEIVED_ERROR":          4,
                
                "ST_ENCRYPT_MESSAGE_REQ_SENT":          5, 
                "ST_ENCRYPT_MESSAGE_REQ_RECEIVED":      6,
                "ST_ENCRYPT_MESSAGE_REQ_ERROR":         7,
                
                "ST_DECRYPT_MESSAGE_REQ_SENT":          8, 
                "ST_DECRYPT_MESSAGE_REQ_RECEIVED":      9,
                "ST_DECRYPT_MESSAGE_REQ_ERROR":         10
                
            };

    //------------------------------------------------------------------------------
    //dateTime 
    //------------------------------------------------------------------------------
    function pad(n) {
        return n<10 ? '0'+n : n;
    }

    function getDateTime() {
        var currentDate = new Date();
        var date = currentDate.getDate();
        var month = currentDate.getMonth(); 
        var year = '' + currentDate.getFullYear();
        var ddmmyyyy = pad(date) + "." + pad(month + 1) + "." + year.substring(2,4);
        var hour = currentDate.getHours();
        var minute = currentDate.getMinutes();
        var second = currentDate.getSeconds();
        var hhmmss = pad(hour) + ":" + pad(minute) + ":" + pad(second);
        return ddmmyyyy + " " + hhmmss;
    }

    //------------------------------------------------------------------------------
    //connectedStatus 
    //------------------------------------------------------------------------------
    function setConnectedStatus(status) {
//        var conStateLoggingElement = $("#connectionStateLog");
//        status = getDateTime() + " - " + status;
//        if(conStateLoggingElement.val() !== '') {
//            status = '\n' + status;
//        };
//        conStateLoggingElement.append(status); 
//        conStateLoggingElement.scrollTop(conStateLoggingElement[0].scrollHeight);
    }

    //------------------------------------------------------------------------------
    //wait for response process 
    //------------------------------------------------------------------------------
    var sendInterval = null;

    function responseWaitingStart(waitTimeout) {
        sendInterval = window.setInterval(function () {
                                        this.responseWaitingTimeoutErrorHandler();
                                    }, waitTimeout);
    }

    function responseWaitingStop() {
        clearInterval(sendInterval);
        sendInterval = null;
    }

    function isAnotherProcessRunning() {
        if(sendInterval !== null) {
            setConnectedStatus("Error. Can't handle operation. Another process is running.");
            return true;
        };
        return false;
    }
    //wait for response process end ------------------------------------------------

    //------------------------------------------------------------------------------
    //Checking input values 
    //------------------------------------------------------------------------------
    function inputDivClassSetError(element) {
        element.parent('div').addClass('has-error');
    }

    function inputDivClassRemoveError(element) {
        element.parent('div').removeClass('has-error');
    }

    //Checking input values end ----------------------------------------------------

    function checkLogged() {
        var cookieValue = $.cookie('loggedStatus');
        if ((cookieValue !== null) && (cookieValue !== undefined)) {
            return true;
        } else {
            window.location = '/logout';        
        }
        return false;
    }

    function showAlert(message) {
        $("#alertMessage").text(message);
        $('#alertMessageDiv').fadeTo(3000, 500).slideUp(500, function() {
            $("#alertMessageDiv").slideUp(500);
        });
    }
    
    UTILS.SystemState = SystemState;

    UTILS.setConnectedStatus = setConnectedStatus;
    
    UTILS.responseWaitingStart = responseWaitingStart;
    UTILS.responseWaitingStop = responseWaitingStop;
    UTILS.isAnotherProcessRunning = isAnotherProcessRunning;
    
    UTILS.setElementsModifier = setElementsModifier;
    UTILS.setResponseWaitingTimeoutErrorHandler = setResponseWaitingTimeoutErrorHandler;
    
    UTILS.inputDivClassSetError = inputDivClassSetError;
    UTILS.inputDivClassRemoveError = inputDivClassRemoveError;
    
    UTILS.checkLogged = checkLogged;
    
    UTILS.showAlert = showAlert;

    return UTILS;

}());
