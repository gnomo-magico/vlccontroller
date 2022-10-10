package com.vlccontroller.vlccontrol;

import com.vlccontroller.dto.VLCInstance;
import java.util.Optional;

public interface VLCInterface {

  Optional<VLCInstance> getInstanceMetaInformation();

  String pauseInstance();

  String playInstance();

  String stopInstance();
}
