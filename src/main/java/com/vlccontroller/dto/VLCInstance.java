package com.vlccontroller.dto;

import java.util.Optional;
import com.vlccontroller.utils.TimeParser;
import com.vlccontroller.utils.VLCVolumeParser;

public class VLCInstance {

  private final VLCInformation information;
  private final String state;
  private final Long time;
  private final Long volume;

  private final Long length;
  private static final String STOPPED_STATE = "stopped";
  private static final String RUNNING_STATE = "playing";

  public VLCInstance(VLCInformation information, String state, Long time, Long volume,
      Long length) {
    this.information = information;
    this.state = state;
    this.time = time;
    this.volume = volume;
    this.length = length;
  }

  public boolean isRunning() {
    return this.state != null && state.equals(RUNNING_STATE);
  }

  public boolean isStopped() {
    return this.state != null && state.equals(STOPPED_STATE);
  }

  public Optional<String> getFileName() {
    return !isStopped() ? Optional.of(information.category.meta.filename)
        : Optional.empty();
  }

  @Override
  public String toString() {
    return "Titolo          :\t" + getFileName().orElse("Uknown") + "\n"
        + "Volume          :\t" + VLCVolumeParser.format(volume) + "\n"
        + "Durata          :\t" + TimeParser.getParsedTime(length) + "\n"
        + "Tempo corrente  :\t" + TimeParser.getParsedTime(time);
  }


  private static class VLCInformation {

    private final VLCCategory category;

    public VLCInformation(VLCCategory category) {
      this.category = category;
    }

    private static class VLCCategory {

      private final VLCMeta meta;

      public VLCCategory(VLCMeta meta) {
        this.meta = meta;
      }

      private static class VLCMeta {

        private final String filename;

        public VLCMeta(String filename) {
          this.filename = filename;
        }
      }
    }
  }


}
