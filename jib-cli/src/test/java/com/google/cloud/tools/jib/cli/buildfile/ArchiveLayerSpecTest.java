/*
 * Copyright 2020 Google LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.cloud.tools.jib.cli.buildfile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.nio.file.Paths;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;

/** Tests for {@link ArchiveLayerSpec}. */
public class ArchiveLayerSpecTest {

  private static final ObjectMapper archiveLayerSpecMapper = new ObjectMapper(new YAMLFactory());

  @Test
  public void testArchiveLayerSpec_full() throws JsonProcessingException {
    String data =
        "name: layer name\n" + "archive: out/archive.tgz\n" + "mediaType: test.media.type";

    ArchiveLayerSpec parsed = archiveLayerSpecMapper.readValue(data, ArchiveLayerSpec.class);
    Assert.assertEquals("layer name", parsed.getName());
    Assert.assertEquals(Paths.get("out/archive.tgz"), parsed.getArchive());
    Assert.assertEquals("test.media.type", parsed.getMediaType().get());
  }

  @Test
  public void testArchiveLayerSpec_nameRequired() {
    String data = "archive: out/archive";

    try {
      archiveLayerSpecMapper.readValue(data, ArchiveLayerSpec.class);
      Assert.fail();
    } catch (JsonProcessingException jpe) {
      MatcherAssert.assertThat(
          jpe.getMessage(), CoreMatchers.startsWith("Missing required creator property 'name'"));
    }
  }

  // With {@link LayerSpec.Deserializer#deserialize} this test seems pointless, but it still helps
  // define the behavior of this class.
  @Test
  public void testArchiveLayerSpec_archiveRequired() {
    String data = "name: layer name";

    try {
      archiveLayerSpecMapper.readValue(data, ArchiveLayerSpec.class);
      Assert.fail();
    } catch (JsonProcessingException jpe) {
      MatcherAssert.assertThat(
          jpe.getMessage(), CoreMatchers.startsWith("Missing required creator property 'archive'"));
    }
  }
}
