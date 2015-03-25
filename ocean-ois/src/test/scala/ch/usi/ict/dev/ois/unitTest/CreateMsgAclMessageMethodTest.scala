package ch.usi.ict.dev.ois.unitTest

import org.scalatest.FunSpec
import org.scalatest.GivenWhenThen
import org.scalatest.Matchers
import ch.usi.ict.dev.AclOverSoapHttpMP.AclMessage
import ch.usi.ict.dev.AclOverSoapHttpMP.Act
import ch.usi.ict.dev.AclOverSoapHttpMP.AgentIdentifier
import ch.usi.ict.dev.ontology.PayDeliveryOntology



class CreateMsgAclMessageMethodTest extends FunSpec with GivenWhenThen with Matchers {
  
  
  
	
	describe("The AclMessage.createMsgFrom(...) helper method"){

		it("should create an ACL Message exhibiting properties reflecting the passed parameters") {
		  
		  Given("a Acl msg variable")
		  	var msg : AclMessage = null 
		  	
		  
		  When("assigned with the value returned by the call to AclMessage.createMsgFrom(...) with the params listed below")
		  	
		    info("sendername = sender0")
		  	val sendername = "sender0"
		  	  
		  	info("receiverName = receiver0")
		  	val receivername = "receiver0"
		  	  
		  	info("senderadress = http://127.0.0.1:146")
		  	val senderadress = "http://127.0.0.1:146"
		  	  
		  	info("receiveradress = http://127.0.0.1:145")
		  	val receiveradress = "http://127.0.0.1:145"
		  	
		  	info("language = OceanCL")
		  	val language = "OceanCL"
		  	  
		  	info("ontology= http://www.people.lu.unisi.ch/okouyad/PayDeliveryOntology.owl#")
		  	val ontology = PayDeliveryOntology.Vocabulary.ONTOLOGY_NAME.URIValue()
		  	
		  	info("replywith = 02")
		  	val replywith = "02"
		  	
		  	info("convid= convId")
		  	val convid = "convId"
		  	
		  	msg = AclMessage.createMsgFrom(Act.INFORM,
		  		  AgentIdentifier.getAgentIdentifierWithParams(sendername, senderadress), 
		  		  AgentIdentifier.getAgentIdentifierWithParams(receivername, receiveradress), 
		  		  language, ontology, replywith, convid)

		        
		  Then("the msg variable should not be null")
		  	msg should not be (null)
		  
		  And("its Act value should: " +  Act.INFORM.value())
		    msg.getAclPayload().getAct() should be (Act.INFORM)
		  
		  And("its Payload sender should be equal to the iniated value: (" + sendername +", " + senderadress +")")
		  	msg.getAclPayload().getSender().getName() should be (sendername)
		    msg.getAclPayload().getSender().getAddress().get(0) should be (senderadress)
		  
		  And("its Payload receiver should be equal to the iniated value: (" + receivername +", " + receiveradress + ")")
		  	msg.getAclPayload().getReceiver().get(0).getName() should be (receivername)
		    msg.getAclPayload().getReceiver().get(0).getAddress().get(0) should be (receiveradress)
	
		  And("its language should be the initiated value: " + language)
		    msg.getAclPayload().getLanguage() should be (language)
		  And("its ontology should be the initiated value: " + ontology)
		    msg.getAclPayload().getOntology() should be (ontology)
		  And("its replywith should be the initiated value: " + replywith)
		    msg.getAclPayload().getReplyWith() should be (replywith)
		  And("its convId should be the initiated value: " +  convid)
		    msg.getAclPayload().getConversationId() should be (convid)
  
		  
		}


	}



}