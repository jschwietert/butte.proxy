// From: https://github.com/eigengo/activator-spray-twitter/
// License: Apache2 per https://github.com/eigengo/activator-spray-twitter/blob/master/LICENSE

package butte.proxy.ext.twitter

import spray.http.HttpRequest

trait TwitterAuthorization {
  def authorize: HttpRequest => HttpRequest
}
