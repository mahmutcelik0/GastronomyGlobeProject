package com.globe.gastronomy.backend.dto;

public abstract class AbstractPopulator<Source, Target> {
    protected abstract Target populate(Source source, Target target);

    protected abstract Source reverseConverter(Target target, Source source);

    public Target populate(Source source) {
        return populate(source, generateTarget());
    }

    public Source reverseConverter(Target target){
        return reverseConverter(target,generateSource());
    }

    public abstract Target generateTarget();

    public abstract Source generateSource();
}
