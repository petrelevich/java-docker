package ru.petrelevich;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
1)
docker run java-docker-centos
docker run java-docker-alpine
docker run java-docker-alpine-musl

2)
docker run -it java-docker-centos /bin/bash
docker run -it java-docker-alpine /bin/bash
docker run -it java-docker-alpine-musl /bin/bash

3)
docker run java-docker-centos
docker run --memory=58m --memory-swap=58m --cpus 2 java-docker-centos
dmesg -T

4)
docker run --memory=100m --memory-swap=100m --cpus 2 -it java-docker-centos /bin/bash
free -h

docker run --memory=100m --memory-swap=100m --cpus 2 -it -v /var/lib/lxcfs/proc/meminfo:/proc/meminfo java-docker-centos /bin/bash

docker run --memory=100m --memory-swap=100m --cpus 2 -v /var/lib/lxcfs/proc/meminfo:/proc/meminfo java-docker-centos

5)
docker run -p1026:1026 java-docker


 */

public class Docker {
    public static void main(String[] args) throws InterruptedException {

        com.sun.management.OperatingSystemMXBean osMBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        System.out.println("availableProcessors:" + Runtime.getRuntime().availableProcessors());
        System.out.println("total physical memory, mb:" + osMBean.getTotalPhysicalMemorySize() / 1024 / 1024);
        System.out.println("totalMemory, mb:" + Runtime.getRuntime().totalMemory() / 1024 / 1024);
        System.out.println("maxMemory, mb:" + Runtime.getRuntime().maxMemory() / 1024 / 1024);
        System.out.println("freeMemory, mb:" + Runtime.getRuntime().freeMemory() / 1024 / 1024);

       //   Thread.sleep(TimeUnit.HOURS.toMillis(1)); //Демонстрация OOM
        List<Long> list = new ArrayList<>();
        for (long idx = 0; idx < Long.MAX_VALUE; idx++) {
            Thread.sleep(1);
            list.add(System.currentTimeMillis());
            list.add(System.currentTimeMillis());
            list.add(System.currentTimeMillis());
        }
        System.out.println(list.size());
    }
}
