import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return a client given it id=CLIENT_ID"
    request {
        headers {
            contentType(applicationJson())
        }
        method GET()
        url("/api/v1/clients/CLIENT_ID")
    }
    response {
        status 200
        body('''
                { 
                    "id" : "CLIENT_ID",
                    "name": "NAME",
                    "nameAr": "NAME_AR" 
                }
        ''')
        headers {
            contentType(applicationJson())
        }
    }
}
