package com.yejh.jcode.base.design.visitor;

public interface IEle {
    void print();

    void accept(Visitor v);
}
