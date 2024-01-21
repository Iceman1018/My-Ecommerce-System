package com.example.trade.order.utils;

public class SnowflakeIdWorker {
    //start time(2023-07-01)
    private final long twepoch=1625097600000L;
    //machine id's 5 digits
    private final long workerIdBits=5L;
    //data center id's 5 digits
    private final long datacenterIdBits=5L;
    //max worker id, result is 31
    private final long maxWorkerId=-1L^(-1L<<workerIdBits);
    //max data center id, result is 31
    private final long maxDatacenterId=-1L^(-1L<<datacenterIdBits);
    //sequence's 12 digits
    private final long sequenceBits=12L;
    //shift the machine id by 12 positions
    private final long workerIdShift=sequenceBits;
    //shift the data center id by 12+5 positions
    private final long datacenterIdShift=sequenceBits+workerIdBits;
    //shift the times by 12+5+5 positions
    private final long timestampLeftShift=sequenceBits+workerIdBits+datacenterIdBits;
    //the mask of sequence
    private final long sequenceMask=-1L^(-1L<<sequenceBits);
    //worker's id
    private long workerId;
    //data center's id
    private long datacenterId;
    //sequence in a second
    private long sequence=0L;
    //last id time
    private long lastTimestamp=-1L;

    public SnowflakeIdWorker(long workerId,long datacenterId){
        if(workerId>maxWorkerId||workerId<0){
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if(datacenterId>maxDatacenterId||datacenterId<0){
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId=workerId;
        this.datacenterId=datacenterId;
    }

    protected long timeGen(){return System.currentTimeMillis();}
    protected long tilNextMillis(long lastTimestamp){
        long timestamp=timeGen();
        while(timestamp<=lastTimestamp){
            timestamp=timeGen();
        }
        return timestamp;
    }
    public synchronized long nextId(){
        long timestamp=timeGen();
        if(timestamp<lastTimestamp){
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp)
            );
        }

        if(lastTimestamp==timestamp){
            sequence=(sequence+1)&sequenceMask;
            if(sequence==0){
                timestamp=tilNextMillis(lastTimestamp);
            }
        }
        else{
            sequence=0L;
        }
        lastTimestamp=timestamp;
        return ((timestamp-twepoch)<<timestampLeftShift) //
                |(datacenterId<<datacenterIdShift) //
                |(workerId<<workerIdShift) //
                |sequence;
    }

    public static void main(String[] args){
        SnowflakeIdWorker idWorker=new SnowflakeIdWorker(26,12);
        for(int i=0;i<1000;i++){
            long id=idWorker.nextId();
            System.out.println(id);
        }
    }

}
