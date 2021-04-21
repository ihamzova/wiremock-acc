import com.github.tomakehurst.wiremock.extension.Parameters
import com.google.common.collect.ImmutableMap
import com.tsystems.tm.acc.wiremock.webhook.*

import java.util.stream.Collectors

def put_3scale_event_post_serve_action() {
    def resource = get_resource(serveEvent.request.url).get()
    def resources = get_resource_list(resource)
    def event = serveEvent.request.getBodyAsString()
    resources.add(event)
    persistence.put(resource, resources)
}

def get_3scale_event_helper() {
    def resource = get_resource(context.get("request.path").toString()).get()
    def resources = persistence.get(resource)
    def event = resources.get(0)
    resources.remove(0)
    persistence.put(resource, resources)
    return event
}

def is_3scale_resource_empty_matcher() {
    get_resource(request.url)
            .map(resource -> persistence.get(resource))
            .map(obj -> {
                if (obj instanceof String) {
                    return ((String) obj).isEmpty()
                } else if (obj instanceof List) {
                    return ((List<?>) obj).isEmpty()
                } else if (obj instanceof Map) {
                    return ((Map<?, ?>) obj).isEmpty()
                } else {
                    return false
                }
            })
            .orElse(true)
}

def is_3scale_resource_not_empty_matcher() {
    return !is_3scale_resource_empty_matcher()
}

def get_subscriber_url_helper() {
    "${System.getenv("API_GATEWAY_URL")}${context.get("request.url").toString().replaceFirst("/pubsub/r", "pubsub/s")}"
}

def put_push_subscriber_info_helper() {
    def resource = get_resource(context.get("request.path").toString()).get()

    def resourceList = get_resource_list(resource)

    def url = context.get("request.headers").get("X-Push-Url").get(0)
    def method = Optional.ofNullable(context.get("request.headers").get("X-Push-Method"))
            .map(h -> h.get(0))
            .orElse("POST")
    def subscriberId = UUID.randomUUID().toString()

    resourceList.add(ImmutableMap.of(
            "url", url,
            "method", method,
            "subscriberId", subscriberId
    ))

    persistence.put(resource, resourceList)

    subscriberId
}

def send_push_events_post_serve_action() {
    def resource = get_resource(serveEvent.request.url).get()
    def resourceList = get_resource_list(resource)

    def event = serveEvent.request.getBodyAsString()

    def definitions = resourceList.stream()
            .map(m -> new WebhookPostServeActionDefinition()
                    .withUrl(m.get("url"))
                    .withMethod(m.get("method"))
                    .withBody(event)
                    .withHeader("Content-Type", "application/json"))
            .collect(Collectors.toList()) as List<WebhookPostServeActionDefinition>
    new WebhooksPostServeAction().doAction(serveEvent, admin, Parameters.of(new WebhooksPostServeActionDefinition(definitions)))
}

def unsubscribe_from_push_events_post_serve_action() {
    def resource = get_resource(serveEvent.request.url).get()
    def resourceList = get_resource_list(resource)

    def subscriberId = serveEvent.request.headers.getHeader("X-Pubsub-Subscriber-Id").values().get(0)

    def filteredResourceList = resourceList.stream()
            .filter(m -> !m.get("subscriberId").equals(subscriberId))
            .collect(Collectors.toList())

    persistence.put(resource, filteredResourceList)
}

// util methods

private List get_resource_list(String resource) {
    def resources = persistence.get(resource)
    if (resources == null) {
        resources = []
    }
    resources
}

private Optional<String> get_resource(url) {
    def resourceIndex = url.indexOf("/pubsub/r")
    resourceIndex = resourceIndex == -1 ? url.indexOf("/pubsub/s") : resourceIndex
    resourceIndex = resourceIndex == -1 ? url.indexOf("/pubsub/p") : resourceIndex

    Optional.of(resourceIndex)
            .filter(i -> i != -1)
            .map(i -> i + "/pubsub/r".length())
            .map(i -> url.substring(i + 1))
}