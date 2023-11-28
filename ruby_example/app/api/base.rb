# frozen_string_literal: true

require 'grape'
require 'grape-swagger'
require_relative 'orders'

module API
  class Base < Grape::API
    mount API::Orders

    add_swagger_documentation(
      api_version: '1.0.0',
      hide_documentation_path: true,
      mount_path: '/api/swagger_doc',
      hide_format: true
    )
  end
end
