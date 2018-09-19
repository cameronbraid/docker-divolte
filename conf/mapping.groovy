mapping {
    def locationUri = parse location() to uri

    // validity

    map duplicate() onto 'detectedDuplicate'
    map corrupt() onto 'detectedCorruption'

    // timestamp

    map timestamp() onto 'timestamp'
    map clientTimestamp() onto 'clientTimestamp'

    map eventParameters().value('environment') onto 'environment'

    // session

    map partyId() onto 'partyId'
    map sessionId() onto 'partySessionId'
    map remoteHost() onto 'remoteHost'
    map firstInSession() onto 'firstInSession'

    // user 

    map eventParameters().value('email') onto 'email'
    map eventParameters().value('username') onto 'username'

    // is

    def boolParam = { name -> parse eventParameters().value(name) to boolean  }
    map boolParam('isAdmin') onto 'isAdmin'
    map boolParam('isOffice') onto 'isOffice'
    map boolParam('isTester') onto 'isTester'

    // ids

    map eventParameters().value('customerId') onto 'customerId'
    map eventParameters().value('sessionId') onto 'sessionId'
    map eventParameters().value('requestId') onto 'requestId'

    // system

    map eventParameters().value('frontendName') onto 'frontendName'
    map eventParameters().value('frontendVersion') onto 'frontendVersion'
    map eventParameters().value('frontendServerId') onto 'frontendServerId'
    map eventParameters().value('backendServerId') onto 'backendServerId'

    // referer / version

    map referer() onto 'referer'
    map cookie('affiliate-referral') onto 'affiliateCode'
    map cookie('reference') onto 'referenceCode'
    map locationUri.query().value('variation') onto 'variation'

    // uri

    map location() onto 'uri'

    map locationUri.scheme() onto 'uriScheme'
    map locationUri.path() onto 'uriPath'
    map locationUri.host() onto 'uriHost'
    map locationUri.port() onto 'uriPort'
    map locationUri.decodedQueryString() onto 'uriQueryString'
    map locationUri.query() onto 'uriQuery'
    map locationUri.decodedFragment() onto 'uriFragment'

    // viewport / screen

    map viewportPixelWidth() onto 'viewportPixelWidth'
    map viewportPixelHeight() onto 'viewportPixelHeight'
    map screenPixelWidth() onto 'screenPixelWidth'
    map screenPixelHeight() onto 'screenPixelHeight'
    map devicePixelRatio() onto 'devicePixelRatio'

    // user agent

    map userAgentString() onto 'userAgent'
    def ua = userAgent()
    map ua.name() onto 'userAgentName'
    map ua.family() onto 'userAgentFamily'
    map ua.vendor() onto 'userAgentVendor'
    map ua.type() onto 'userAgentType'
    map ua.version() onto 'userAgentVersion'
    map ua.deviceCategory() onto 'userAgentDeviceCategory'
    map ua.osFamily() onto 'userAgentOsFamily'
    map ua.osVersion() onto 'userAgentOsVersion'
    map ua.osVendor() onto 'userAgentOsVendor'



    // event type

    map eventType() onto 'eventType'

    section {

        // event type specific data
        
        when eventType().equalTo("pageView") apply {
            map pageViewId() onto 'pageViewId'

            def pageStatusCode = parse eventParameters().value('pageStatusCode') to int
            map eventParameters().value('pageTitle') onto 'pageTitle'
            map pageStatusCode onto 'pageStatusCode'

            //  "navigation" | "hashchange" | "popstate" - see src/nitro/types/NavigationTrigger.ts
            map eventParameters().value('pageNavigationTrigger') onto 'pageNavigationTrigger'

            // page type

            // CMS driven: homePage, outboundCountryHomePage, locationLandingPage, mediaHubLocationLandingPage, supplierPage, vehiclePage, promotionPage, cmsPage
            // Frontend driven : searchPage, detailsPage, bookPage, paymentPage, confirmationPage
            map eventParameters().value('pageType') onto 'pageType'
            // llp ref, supplier page ref etc.
            map eventParameters().value('pageTypeRef') onto 'pageTypeRef'


            exit()
        }

        when eventType().equalTo("orderConversion") apply {
            map eventParameters().value('orderKey') onto 'orderKey'
            exit()
        }

        when eventType().equalTo("relocationConversion") apply {
            map eventParameters().value('relocationRef') onto 'relocationRef'
            exit()
        }

        when eventType().equalTo("priceWatchConversion") apply {
            exit()
        }

        when eventType().equalTo("abTestParticipation") apply {
            map eventParameters().value('abTestExperiment') onto 'abTestExperiment'
            map eventParameters().value('abTestAlternative') onto 'abTestAlternative'
            exit()
        }

        when eventType().equalTo("abTestConversion") apply {
            map eventParameters().value('abTestExperiment') onto 'abTestExperiment'
            map eventParameters().value('abTestAlternative') onto 'abTestAlternative'
            exit()
        }

        when eventType().equalTo("hiccupConversion") apply {
            map eventParameters().value('hiccupRef') onto 'hiccupRef'
            exit()
        }

    }
}