mapping {

    // def splitLocation = { encodedLocation ->
        
    //     if ( encodedLocation == null || encodedLocation.trim() == '') return [type:null, name: null, value: null]

    //     def parts = encodedLocation.split(";")

    //     return parts.size() == 1 
    //         ? [type: 'loc',    name: encodedLocation, value: encodedLocation]
    //         : [type: parts[0], name: parts[1],        value: parts[2]]
    // }


    def boolParam = { name -> 
        parse eventParameters().value(name) to boolean
    }

    def mapOptionalSringParam = { name -> 
        def eventParam = eventParameters().value(name)
        when eventParam.isPresent() apply {
            map eventParam onto name
        }
    }
    def mapOptionalSringParamOnto = { paramName, ontoParamName -> 
        def eventParam = eventParameters().value(paramName)
        when eventParam.isPresent() apply {
            map eventParam onto ontoParamName
        }
    }

    def sringParamValue = { name ->
        def eventParam = eventParameters().value(name)
    }
    def locationUri = parse location() to uri

    // validity

    map duplicate() onto 'detectedDuplicate'
    map corrupt() onto 'detectedCorruption'

    // timestamp

    map timestamp() onto 'timestamp'
    map clientTimestamp() onto 'clientTimestamp'

    mapOptionalSringParam('environment')

    // session

    map partyId() onto 'partyId'
    map sessionId() onto 'partySessionId'
    map remoteHost() onto 'remoteHost'
    map firstInSession() onto 'firstInSession'

    // user 

    mapOptionalSringParam('email')
    mapOptionalSringParam('username')

    // is

    map boolParam('isAdmin') onto 'isAdmin'
    map boolParam('isOffice') onto 'isOffice'
    map boolParam('isTester') onto 'isTester'

    // ids

    mapOptionalSringParam('customerId')
    mapOptionalSringParam('sessionId')
    mapOptionalSringParam('requestId')

    // system

    mapOptionalSringParam('frontendName')
    mapOptionalSringParam('frontendVersion')
    mapOptionalSringParam('frontendServerId')
    mapOptionalSringParam('backendServerId')

    // referer / version
    def refererUri
    when eventParameters().value("referer").isPresent() apply {
        map eventParameters().value("referer") onto 'referer'
        refererUri = parse eventParameters().value("referer") to uri
    }
    when eventParameters().value("referer").isAbsent() apply {
        map referer() onto 'referer'
        parse referer() to uri
    }

    map refererUri.scheme() onto 'refererScheme'
    map refererUri.path() onto 'refererPath'
    map refererUri.host() onto 'refererHost'
    map refererUri.port() onto 'refererPort'
    map refererUri.decodedQueryString() onto 'refererQueryString'
    map refererUri.query() onto 'refererQuery'
    map refererUri.decodedFragment() onto 'refererFragment'

    mapOptionalSringParam('affiliateCode')
    map cookie('reference') onto 'referenceCode'
    map locationUri.query().value('variation') onto 'variation'

    mapOptionalSringParam('utmSource')
    mapOptionalSringParam('utmMedium')
    mapOptionalSringParam('utmTerm')
    mapOptionalSringParam('utmContent')
    mapOptionalSringParam('utmCampaign')


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

    mapOptionalSringParam('countryCode')
    mapOptionalSringParam('locationCountryCode')
    mapOptionalSringParam('vehicleCategory')

    mapOptionalSringParam('pickupDate')
    mapOptionalSringParam('dropoffDate')

    mapOptionalSringParam('pickupLocation')
    mapOptionalSringParam('pickupLocationType')
    mapOptionalSringParam('pickupLocationName')
    mapOptionalSringParam('pickupLocationValue')

    mapOptionalSringParam('dropoffLocation')
    mapOptionalSringParam('dropoffLocationType')
    mapOptionalSringParam('dropoffLocationName')
    mapOptionalSringParam('dropoffLocationValue')

    // def splitPickup = splitLocation(sringParamValue('pickupLocation'))
    // map splitPickup.type onto 'pickupLocationType'
    // map splitPickup.name onto 'pickupLocationName'
    // map splitPickup.value onto 'pickupLocationValue'

    // mapOptionalSringParam('dropoffLocation')
    // def splitDropoff = splitLocation(sringParamValue('dropoffLocation'))
    // map splitDropoff.type onto 'dropoffLocationType'
    // map splitDropoff.name onto 'dropoffLocationName'
    // map splitDropoff.value onto 'dropoffLocationValue'

    mapOptionalSringParam('vehicle')

    // event type

    map eventType() onto 'eventType'

    section {

        // event type specific data
        
        when eventType().equalTo("pageView") apply {
            map pageViewId() onto 'pageViewId'

            def pageStatusCode = parse eventParameters().value('pageStatusCode') to int
            mapOptionalSringParam('pageTitle')
            map pageStatusCode onto 'pageStatusCode'

            //  "navigation" | "hashchange" | "popstate" - see src/nitro/types/NavigationTrigger.ts
            mapOptionalSringParam('pageNavigationTrigger')

            // page type

            // CMS driven: homePage, outboundCountryHomePage, locationLandingPage, mediaHubLocationLandingPage, supplierPage, vehiclePage, promotionPage, cmsPage
            // Frontend driven : searchPage, detailsPage, bookPage, paymentPage, confirmationPage
            mapOptionalSringParam('pageType')
            // llp ref, supplier page ref etc.
            mapOptionalSringParam('pageTypeRef')


            exit()
        }

        when eventType().equalTo("orderConversion") apply {
            mapOptionalSringParam('orderKey')
            exit()
        }

        when eventType().equalTo("relocationConversion") apply {
            mapOptionalSringParam('relocationRef')
            exit()
        }

        when eventType().equalTo("priceWatchConversion") apply {
            exit()
        }

        when eventType().equalTo("abTestParticipation") apply {
            mapOptionalSringParam('abTestExperiment')
            mapOptionalSringParam('abTestAlternative')
            exit()
        }

        when eventType().equalTo("abTestConversion") apply {
            mapOptionalSringParam('abTestExperiment')
            mapOptionalSringParam('abTestAlternative')
            exit()
        }

        when eventType().equalTo("hiccupConversion") apply {
            mapOptionalSringParam('hiccupRef')
            exit()
        }

    }
}