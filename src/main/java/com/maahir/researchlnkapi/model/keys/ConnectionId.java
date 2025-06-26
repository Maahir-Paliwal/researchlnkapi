package com.maahir.researchlnkapi.model.keys;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionId implements Serializable {
    private Long connectorId;
    private Long connecteeId;

    @Override
    public boolean equals(Object o){
        if (this == o ) return true;
        if (!(o instanceof ConnectionId)) return false;

        //cast it to be an object of ConnectionId
        ConnectionId connectionIdObject = (ConnectionId) o;

        return Objects.equals(connectorId, connectionIdObject.connectorId) &&
                Objects.equals(connecteeId, connectionIdObject.connecteeId);
    }

    @Override
    public int hashCode(){
        return Objects.hash(connectorId, connecteeId);
    }
}
