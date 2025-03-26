// import
// 必要なmoduleのimport
// k6は独自のJS runtimeを用いておりNode.jsには依存していない点に注意
import http from 'k6/http';
import { sleep } from 'k6';

// opption
// テストの細かい設定を行う
// https://grafana.com/docs/k6/latest/using-k6/k6-options/reference/#vus
export const options = {
  iterations: 100,
  vus: 10
};

// Defalut function
// テストのシナリオを記述する
export default function () {
  // Make a GET request to the target URL
  http.get('http://localhost:8080');

  // Sleep for 1 second to simulate real-world usage
  sleep(1);
}

// Lifecycle operation
// テストの前後などに行う処理を記述する