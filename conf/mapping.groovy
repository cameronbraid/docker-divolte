mapping {

    def splitLocation = { encodedLocation ->
        
        if ( encodedLocation == null || encodedLocation.trim() == '') return [type:null, name: null, value: null]

        def parts = loc.split(";")

        return parts.size() == 1 
            ? [type: 'loc',    name: encodedLocation, value: encodedLocation]
            : [type: parts[0], name: parts[1],        value: parts[2]]
    }


    def boolParam = { name -> 
        parse eventParameters().value(name) to boolean
    }

    def stringOrNullParam = { name -> 
        def val = (eventParameters().value(name) ?: '').trim()
        return val == '' ? null : val
    }

    def locationUri = parse location() to uri

    // validity

    map duplicate() onto 'detectedDuplicate'
    map corrupt() onto 'detectedCorruption'

    // timestamp

    map timestamp() onto 'timestamp'
    map clientTimestamp() onto 'clientTimestamp'

    map stringOrNullParam('environment') onto 'environment'

    // session

    map partyId() onto 'partyId'
    map sessionId() onto 'partySessionId'
    map remoteHost() onto 'remoteHost'
    map firstInSession() onto 'firstInSession'

    // user 

    map stringOrNullParam('email') onto 'email'
    map stringOrNullParam('username') onto 'username'

    // is

    map boolParam('isAdmin') onto 'isAdmin'
    map boolParam('isOffice') onto 'isOffice'
    map boolParam('isTester') onto 'isTester'

    // ids

    map stringOrNullParam('customerId') onto 'customerId'
    map stringOrNullParam('sessionId') onto 'sessionId'
    map stringOrNullParam('requestId') onto 'requestId'

    // system

    map stringOrNullParam('frontendName') onto 'frontendName'
    map stringOrNullParam('frontendVersion') onto 'frontendVersion'
    map stringOrNullParam('frontendServerId') onto 'frontendServerId'
    map stringOrNullParam('backendServerId') onto 'backendServerId'

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


    // site context

    map stringOrNullParam('countryCode') onto 'countryCode'
    map stringOrNullParam('locationCountryCode') onto 'locationCountryCode'
    map stringOrNullParam('vehicleCategory') onto 'vehicleCategory'

    map stringOrNullParam('pickupDate') onto 'pickupDate'
    map stringOrNullParam('dropoffDate') onto 'dropoffDate'

    map stringOrNullParam('pickupLocation') onto 'pickupLocation'
    
    def splitPickup = splitLocation(stringOrNullParam('pickupLocation'))
    map splitPickup.type onto 'pickupLocationType'
    map splitPickup.name onto 'pickupLocationName'
    map splitPickup.value onto 'pickupLocationValue'

    map stringOrNullParam('dropoffLocation') onto 'dropoffLocation'
    def splitDropoff = splitLocation(stringOrNullParam('dropoffLocation'))
    map splitDropoff.type onto 'dropoffLocationType'
    map splitDropoff.name onto 'dropoffLocationName'
    map splitDropoff.value onto 'dropoffLocationValue'

    map stringOrNullParam('vehicle') onto 'vehicle'

    // event type

    map eventType() onto 'eventType'

    section {

        // event type specific data
        
        when eventType().equalTo("pageView") apply {
            map pageViewId() onto 'pageViewId'

            def pageStatusCode = parse eventParameters().value('pageStatusCode') to int
            map stringOrNullParam('pageTitle') onto 'pageTitle'
            map pageStatusCode onto 'pageStatusCode'

            //  "navigation" | "hashchange" | "popstate" - see src/nitro/types/NavigationTrigger.ts
            map stringOrNullParam('pageNavigationTrigger') onto 'pageNavigationTrigger'

            // page type

            // CMS driven: homePage, outboundCountryHomePage, locationLandingPage, mediaHubLocationLandingPage, supplierPage, vehiclePage, promotionPage, cmsPage
            // Frontend driven : searchPage, detailsPage, bookPage, paymentPage, confirmationPage
            map stringOrNullParam('pageType') onto 'pageType'
            // llp ref, supplier page ref etc.
            map stringOrNullParam('pageTypeRef') onto 'pageTypeRef'


            exit()
        }

        when eventType().equalTo("orderConversion") apply {
            map stringOrNullParam('orderKey') onto 'orderKey'
            exit()
        }

        when eventType().equalTo("relocationConversion") apply {
            map stringOrNullParam('relocationRef') onto 'relocationRef'
            exit()
        }

        when eventType().equalTo("priceWatchConversion") apply {
            exit()
        }

        when eventType().equalTo("abTestParticipation") apply {
            map stringOrNullParam('abTestExperiment') onto 'abTestExperiment'
            map stringOrNullParam('abTestAlternative') onto 'abTestAlternative'
            exit()
        }

        when eventType().equalTo("abTestConversion") apply {
            map stringOrNullParam('abTestExperiment') onto 'abTestExperiment'
            map stringOrNullParam('abTestAlternative') onto 'abTestAlternative'
            exit()
        }

        when eventType().equalTo("hiccupConversion") apply {
            map stringOrNullParam('hiccupRef') onto 'hiccupRef'
            exit()
        }

    }
}