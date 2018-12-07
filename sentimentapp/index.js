/**
https://github.com/manojknit/tfjs-examples/tree/master/sentiment
 */

import * as tf from '@tensorflow/tfjs';
import * as loader from './loader';
import * as ui from './ui';


const HOSTED_URLS = {
  model:
      'https://storage.googleapis.com/tfjs-models/tfjs/sentiment_cnn_v1/model.json',
  metadata:
      'https://storage.googleapis.com/tfjs-models/tfjs/sentiment_cnn_v1/metadata.json'
};

const LOCAL_URLS = {
  model: 'http://localhost:1235/resources/model.json',
  metadata: 'http://localhost:1235/resources/metadata.json'
};

//Spinner


//Star Rating
function setStart(startsrat)
{
  //http://www.ulmanen.fi/stuff/stars.php
  $( "#starrating" ).html('<span class="stars">'+parseFloat(startsrat)+'</span>');
  $('span.stars').stars();
}

$.fn.stars = function() {
return $(this).each(function() {
  $(this).html($('<span />').width(Math.max(0, (Math.min(5, parseFloat($(this).html())))) * 16));
  });
}

class SentimentPredictor {
  /**
   * Initializes the Sentiment demo.
   */
  async init(urls) {
    this.urls = urls;
    this.model = await loader.loadHostedPretrainedModel(urls.model);
    await this.loadMetadata();
    return this;
  }

  async loadMetadata() {
    const sentimentMetadata =
        await loader.loadHostedMetadata(this.urls.metadata);
    ui.showMetadata(sentimentMetadata);
    this.indexFrom = sentimentMetadata['index_from'];
    this.maxLen = sentimentMetadata['max_len'];
    console.log('indexFrom = ' + this.indexFrom);
    console.log('maxLen = ' + this.maxLen);

    this.wordIndex = sentimentMetadata['word_index']
  }

  predict(text) {
    // Convert to lower case and remove all punctuations.
    const inputText =
        text.trim().toLowerCase().replace(/(\.|\,|\!)/g, '').split(' ');
    // Look up word indices.
    const inputBuffer = tf.buffer([1, this.maxLen], 'float32');
    for (let i = 0; i < inputText.length; ++i) {
      // TODO(cais): Deal with OOV words.
      const word = inputText[i];
      inputBuffer.set(this.wordIndex[word] + this.indexFrom, 0, i);
    }
    const input = inputBuffer.toTensor();

    ui.status('Running inference');
    const beginMs = performance.now();
    const predictOut = this.model.predict(input);
    const score = predictOut.dataSync()[0];
    predictOut.dispose();
    const endMs = performance.now();

    return {score: score, elapsed: (endMs - beginMs)};
  }
};


/**
 * Loads the pretrained model and metadata, and registers the predict
 * function with the UI.
 */
async function setupSentiment() {
  if (await loader.urlExists(HOSTED_URLS.model)) {
    ui.status('Model available: ' + HOSTED_URLS.model);
    const button = document.getElementById('load-pretrained-remote');
    button.addEventListener('click', async () => {
      const predictor = await new SentimentPredictor().init(HOSTED_URLS);
      ui.prepUI(x => predictor.predict(x));
    });
    button.style.display = 'inline-block';
  }

  if (await loader.urlExists(LOCAL_URLS.model)) {
    ui.status('Model available: ' + LOCAL_URLS.model);
    const button = document.getElementById('load-pretrained-local');
    button.addEventListener('click', async () => {
      const predictor = await new SentimentPredictor().init(LOCAL_URLS);
      ui.prepUI(x => predictor.predict(x));
    });
    button.style.display = 'inline-block';
  }

  ui.status('Standing by.');
}

async function initPageFunction() {
    const button = document.getElementById('btnAnalyze');
    button.addEventListener('click', async () => {
      const predictor = await new SentimentPredictor().init(HOSTED_URLS);
      const reviewText = document.getElementById('review-text');
      const result = predictor.predict(reviewText.value);
      let confidence = result.score.toFixed(2)*100;
      let statusText = 'Inference result (0 - negative; 1 - positive): ' + confidence + '%' ;//+
      confidence = confidence/20;
      setStart(confidence);
      //' (elapsed: ' + result.elapsed + ' ms)';
      console.log(statusText);
      document.getElementById('status').textContent = statusText;     
    });
    button.style.display = 'inline-block';
    document.getElementById('status').textContent = 'Standing by.';
}

//setupSentiment();
initPageFunction();

