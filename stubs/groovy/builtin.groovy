def put_3scale_event_post_serve_action() {
    def resource = serveEvent.getRequest().getUrl().substring(9);
    def resources = persistence.get(resource);
    if (resources == null) {
        resources = []
    }
    def event = serveEvent.getRequest().getBodyAsString();
    resources.add(event);
    persistence.put(resource, resources);
}

def get_3scale_event_helper() {
    def resource = context.get("request.path").toString().substring(9);
    def resources = persistence.get(resource);
    def event = resources.get(0);
    resources.remove(0);
    persistence.put(resource, resources);
    return event;
}

def is_3scale_resource_empty_matcher() {
    def resource = request.getUrl().substring(9);
    Object obj = persistence.get(resource);
    if (obj == null) {
        return true;
    } else if (obj instanceof String) {
        return ((String) obj).isEmpty();
    } else if (obj instanceof List) {
        return ((List<?>) obj).isEmpty();
    } else if (obj instanceof Map) {
        return ((Map<?, ?>) obj).isEmpty();
    } else {
        return false;
    }
}

def is_3scale_resource_not_empty_matcher() {
    return !is_3scale_resource_empty_matcher();
}