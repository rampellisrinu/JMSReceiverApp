import org.apache.activemq.broker.TransportConnector
import org.apache.activemq.usage.StoreUsage
import org.apache.activemq.usage.SystemUsage
import org.apache.activemq.usage.TempUsage
import org.apache.activemq.xbean.XBeanBrokerService
import org.springframework.jms.connection.SingleConnectionFactory

// Place your Spring DSL code here
beans = {

    activeMQTempUsage(TempUsage) {
        activeMQTempUsage.limit = 1024 * 1024 * 512 // property value in mb
    }

    activeMQStoreUsage(StoreUsage) {
        activeMQStoreUsage.limit = 1024 * 1024 * 512 // property value in mb
    }

    activeMQSystemUsage(SystemUsage){
        activeMQSystemUsage.tempUsage = ref('activeMQTempUsage')
        activeMQSystemUsage.storeUsage = ref('activeMQStoreUsage')
    }

    tcpConnector(TransportConnector,uri:'tcp://localhost:61616'){
    }

    connectors(ArrayList,[ref('tcpConnector')]){
    }

    myBrokerService(XBeanBrokerService){bean->
        myBrokerService.useJmx = false
        myBrokerService.persistent = true
        myBrokerService.dataDirectory = 'my-activemq_data'
        myBrokerService.systemUsage = ref('activeMQSystemUsage')
        myBrokerService.transportConnectors = ref('connectors')
    }

    amqConnectionFactory(org.apache.activemq.ActiveMQConnectionFactory) {
        brokerURL = 'vm://localhost'
    }

    jmsConnectionFactory(SingleConnectionFactory) {  bean ->
        targetConnectionFactory = ref(amqConnectionFactory)
    }

    /*jmsConnectionFactory(SingleConnectionFactory) {
        targetConnectionFactory = { ActiveMQConnectionFactory cf ->
            brokerURL = "tcp://localhost:61616"
        }
    }*/
}
