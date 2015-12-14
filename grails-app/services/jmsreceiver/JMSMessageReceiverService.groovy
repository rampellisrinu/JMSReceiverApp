package jmsreceiver

import grails.plugin.jms.Queue


class JMSMessageReceiverService {

    static exposes = ["jms"]

    @Queue(name = "queue.notification")
    def receiveMessage(message){
        log.info "message received.."+message
    }
}
